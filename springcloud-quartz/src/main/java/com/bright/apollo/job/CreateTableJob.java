package com.bright.apollo.job;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TCreateTableSql;
import com.bright.apollo.constant.SubTableConstant;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.hystrix.HystrixFeignUserFallback;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.DateHelper;
import com.zz.common.exception.AppException;
import com.zz.common.util.StringUtils;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月25日
 * @Version:1.1.0
 */
public class CreateTableJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(CreateTableJob.class);
	@Autowired(required = true)
	private FeignUserClient feignUserClient;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("====execute start====");
		try {
			String nowMonth = DateHelper.formatDate(new Date().getTime(), DateHelper.FORMATMONTH);
			String nextMonth = DateHelper.formatDate(DateHelper.getTomorrow(), DateHelper.FORMATMONTH);
			if (!nowMonth.equals(nextMonth)) {
				logger.info("====create t_device_status====");
				createTable(SubTableConstant.T_DEVICE_STATUS, SubTableConstant.T_DEVICE_STATUS_SUFFIX, nextMonth, "");
				logger.info("====create t_user_operation====");
				createTable(SubTableConstant.T_USER_OPERATION, SubTableConstant.T_USER_OPERATION_SUFFIX, nextMonth, "");
				logger.info("====create t_device_status_merge====");
				feignUserClient.dropTable(SubTableConstant.T_DEVICE_STATUS_MERGE);
				// CreateTableLogBussiness
				// .dropTable(SubTableConstant.T_DEVICE_STATUS_MERGE);
				ResponseObject<List<TCreateTableLog>> createTableLogRes = feignUserClient
						.listCreateTableLogByNameWithLike(SubTableConstant.T_DEVICE_STATUS);
				// List<TCreateTableLog> list = CreateTableLogBussiness
				// .listCreateTableLogByNameWithLike(SubTableConstant.T_DEVICE_STATUS);
				if (createTableLogRes != null && createTableLogRes.getData() != null
						&& createTableLogRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
					// if (list != null && list.size() > 0) {
					List<TCreateTableLog> list = createTableLogRes.getData();
					StringBuilder auxiliary = new StringBuilder("UNION=(");
					for (int i = 0; i < list.size(); i++) {
						auxiliary.append(list.get(i).getName()).append(",");
					}
					String str = auxiliary.toString().substring(0, auxiliary.toString().length() - 1)
							+ ") INSERT_METHOD=LAST";
					createTable(SubTableConstant.T_DEVICE_STATUS_MERGE, "", "", str);
				}
			}
		} catch (Exception e) {
			logger.error("===create table error===");
			e.printStackTrace();
		}
		logger.info("====execute end====");
	}

	private void createTable(String tableName, String suffix, String nextMonth, String auxiliary) throws AppException {
		String createTableSql = getCreateTableSql(tableName, nextMonth, auxiliary);
		feignUserClient.createTable(createTableSql);
		//CreateTableLogBussiness.createTable(createTableSql);
		if (!StringUtils.isEmpty(nextMonth)) {
			TCreateTableLog tCreateTableLog = new TCreateTableLog();
			tCreateTableLog.setName(suffix + nextMonth);
			feignUserClient.addCreateTableLog(tCreateTableLog);
		//	CreateTableLogBussiness.addCreateTableLog(tCreateTableLog);
		}
	}

	public  String getCreateTableSql(String name, String suffix, String auxiliary) {
		//TCreateTableSql tCreateTableSql = null;
		try {
			ResponseObject<TCreateTableSql> sqlRes = feignUserClient.queryTCreateTableSqlByprefix(name);
			if(sqlRes!=null&&sqlRes.getData()!=null&&sqlRes.getStatus()==ResponseEnum.SelectSuccess.getStatus()&& !StringUtils.isEmpty(sqlRes.getData().getSuffix())){
			//if (tCreateTableSql != null && !StringUtils.isEmpty(tCreateTableSql.getSuffix())) {
				if (StringUtils.isEmpty(suffix)) {
					return new StringBuilder("CREATE TABLE ").append(name).append(sqlRes.getData().getSuffix())
							.append(auxiliary).toString();
				}
				return new StringBuilder("CREATE TABLE ").append(name).append("_").append(suffix)
						.append(sqlRes.getData().getSuffix()).append(auxiliary).toString();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
}
