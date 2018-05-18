package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.service.base.BasicService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月8日  
 *@Version:1.1.0  
 */
public interface UserService extends BasicService{

	/**  
	 * @param name
	 * @return  
	 * @Description:  
	 */
	TUser queryUserByName(String name);

	/**  
	 * @param username
	 * @return  
	 * @Description:  
	 */
	TUser queryUserByOpenId(String openId);

	/**  
	 * @param object
	 * @param object2
	 * @param object3  
	 * @Description:  
	 */
	int saveUserByWeiXinInfo(String openid, String headimgurl, String nickname);

	/**  
	 * @param tUserByMobile  
	 * @Description:  
	 */
	int updateUser(TUser tUserByMobile);

	/**  
	 * @param tUser  
	 * @Description:  
	 */
	int deleteUserById(Integer userId);

	/**  
	 * @param WxUserId
	 * @param MobileUserId  
	 * @Description:  
	 */
	List<TUserObox> getListOfUserObox(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param tUserOboxs  
	 * @Description:  
	 */
	int insertBatchByUserObox(List<TUserObox> tUserOboxs);

	/**  
	 * @param id
	 * @param id2  
	 * @Description:  
	 */
	List<TUserObox> getListOverPrivilegeOfUserObox(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param list  
	 * @Description:  
	 */
	int updateBatchByUserObox(List<TUserObox> list);

	/**  
	 * @param WxUserId
	 * @param MobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> getListOfUserDevice(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param tUserDevices  
	 * @Description:  
	 */
	int insertBatchByUserDevice(List<TUserDevice> tUserDevices);

	/**  
	 * @param WxUserId
	 * @param MobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> getListOverPrivilegeOfUserDevice(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param list2  
	 * @Description:  
	 */
	int updateBatchByUserDevice(List<TUserDevice> list2);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	List<TUserObox> queryUserOboxByUserId(Integer id);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> queryUserDeviceByUserId(Integer id);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	List<TUserScene> queryUserSceneByUserId(Integer id);

	TUser getUserByUserId(int id);

	List<TUser> getUsersBySceneNumber(int sceneNumber);

	List<TUser> getUsersByDeviceId(int configDeviceId);
}
