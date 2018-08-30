package com.bright.apollo.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.zz.common.util.StringUtils;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月16日
 * @Version:1.1.0
 */
public class TimeJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(TimeJob.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Autowired(required=true)
	private FeignSceneClient feignSceneClient;

	@Autowired(required=true)
	private FeignAliClient feignAliClient;

	@Autowired(required=true)
	private FeignDeviceClient feignDeviceClient;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("=============TimeJob execute================");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String sceneNumber = dataMap.getString("scene_number");
		String conditionGroup = dataMap.getString("condition_group");
		log.info("==sceneNumber:" + sceneNumber + " ==conditionGroup:" + conditionGroup);
		if (!StringUtils.isEmpty(sceneNumber)) {
			try {
				ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(Integer.parseInt(sceneNumber));
				if (sceneRes == null || sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
						|| sceneRes.getData() == null || sceneRes.getData().getSceneStatus() == 0
						|| sceneRes.getData().getSceneRun() == 1) {
					log.info("====scene not exist====");
					return;
				}
				TScene tScene = sceneRes.getData();
				ResponseObject<List<TSceneCondition>> sceneConditionsRes = feignSceneClient
						.getSceneConditionsBySceneNumberAndConditionGroup(Integer.parseInt(sceneNumber),
								Integer.parseInt(conditionGroup));
				if (sceneConditionsRes != null && sceneConditionsRes.getData() != null
						&& sceneConditionsRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& sceneConditionsRes.getData().size() == 1) {
					log.info("====condition size one====");
					tScene.setSceneRun((byte) 1);
					feignSceneClient.updateScene(tScene);
					feignAliClient.addSceneAction(tScene.getSceneNumber());
					// sceneActionThreadPool.addSceneAction(tScene.getSceneNumber());
				} else if (sceneConditionsRes != null && sceneConditionsRes.getData() != null
						&& sceneConditionsRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& sceneConditionsRes.getData().size() > 1) {
					// mutile condition
					boolean matchCon = true;
					for (TSceneCondition tSceneCondition2 : sceneConditionsRes.getData()) {

						if (tSceneCondition2.getSerialid() != null) {
							ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
									.getDevice(tSceneCondition2.getSerialid());
							// TOboxDeviceConfig oboxDeviceConfig =
							// DeviceBusiness
							// .queryDeviceConfigBySerialID(tSceneCondition2.getSerialId());
							if (deviceRes != null && deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
									&& deviceRes.getData() != null) {
								log.info("====condition size mutile match start====");
								String cond = tSceneCondition2.getCond();
								for (int i = 0; i < cond.length(); i += 4) {
									int index = Integer.parseInt(cond.substring(i, i + 2), 16);
									// if (index == 0) {
									// break;
									// }
									int vaildLen = (index >> 3) & 0x07;
									String condState = cond.substring(i + 2, i + 2 + vaildLen * 2);
									String updateState = deviceRes.getData().getDeviceState().substring(i + 2,
											i + 2 + vaildLen * 2);
									if (vaildLen > 1) {
										i += 2 * (vaildLen - 1);
									}
									if ((index & 0x07) == 0x01) {
										// <
										if (Integer.parseInt(updateState, 16) < Integer.parseInt(condState, 16)) {

										} else {
											matchCon = false;
											break;
										}
									} else if ((index & 0x07) == 0x02) {
										// ==
										if (Integer.parseInt(updateState, 16) == Integer.parseInt(condState, 16)) {

										} else {
											matchCon = false;
											break;
										}
									} else if ((index & 0x07) == 0x03) {
										// <=
										if (Integer.parseInt(updateState, 16) <= Integer.parseInt(condState, 16)) {

										} else {
											matchCon = false;
											break;
										}

									} else if ((index & 0x07) == 0x04) {
										// >
										if (Integer.parseInt(updateState, 16) > Integer.parseInt(condState, 16)) {

										} else {
											matchCon = false;
											break;
										}

									} else if ((index & 0x07) == 0x06) {
										// >=
										if (Integer.parseInt(updateState, 16) >= Integer.parseInt(condState, 16)) {

										} else {
											matchCon = false;
											break;
										}

									}
								}
							}
						}
					}
					if (matchCon) {
						log.info("====condition size mutile match true====");
						ResponseObject<List<TSceneAction>> sceneActionRes = feignSceneClient
								.getSceneActionsBySceneNumber(Integer.parseInt(sceneNumber));
						// List<TSceneAction> tSceneActions = SceneBusiness
						// .querySceneActionsBySceneNumber(Integer.parseInt(sceneNumber));
						if (sceneActionRes != null && sceneActionRes.getData() != null
								&& sceneActionRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()){
							//if (tSceneActions != null) {
								tScene.setSceneRun((byte) 1);
								feignSceneClient.updateScene(tScene);
								//SceneBusiness.updateScene(tScene);
								//sceneActionThreadPool.addSceneAction(tScene.getSceneNumber());
								feignAliClient.addSceneAction(tScene.getSceneNumber());
						}
					}
				}

			} catch (Exception e) {
				log.error("===error msg:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
