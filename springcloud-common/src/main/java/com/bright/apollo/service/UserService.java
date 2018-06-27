package com.bright.apollo.service;

import java.util.List;

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
	@Deprecated
	int saveUserByWeiXinInfo(String openid, String headimgurl, String nickname);

	/**  
	 * @param tUserByMobile  
	 * @Description:  
	 */
	@Deprecated
	int updateUser(TUser tUserByMobile);

	/**  
	 * @param tUser  
	 * @Description:  
	 */
	@Deprecated
	int deleteUserById(Integer userId);

	/**  
	 * @param WxUserId
	 * @param MobileUserId  
	 * @Description:  
	 */
	@Deprecated
	List<TUserObox> getListOfUserObox(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param tUserOboxs  
	 * @Description:  
	 */
	@Deprecated
	int insertBatchByUserObox(List<TUserObox> tUserOboxs);

	/**  
	 * @param id
	 * @param id2  
	 * @Description:  
	 */
	@Deprecated
	List<TUserObox> getListOverPrivilegeOfUserObox(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param list  
	 * @Description:  
	 */
	@Deprecated
	int updateBatchByUserObox(List<TUserObox> list);

	/**  
	 * @param WxUserId
	 * @param MobileUserId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TUserDevice> getListOfUserDevice(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param tUserDevices  
	 * @Description:  
	 */
	@Deprecated
	int insertBatchByUserDevice(List<TUserDevice> tUserDevices);

	/**  
	 * @param WxUserId
	 * @param MobileUserId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TUserDevice> getListOverPrivilegeOfUserDevice(Integer WxUserId, Integer MobileUserId);

	/**  
	 * @param list2  
	 * @Description:  
	 */
	@Deprecated
	int updateBatchByUserDevice(List<TUserDevice> list2);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TUserObox> queryUserOboxByUserId(Integer id);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TUserDevice> queryUserDeviceByUserId(Integer id);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TUserScene> queryUserSceneByUserId(Integer id);

	TUser getUserByUserId(int id);

	List<TUser> getUsersBySceneNumber(int sceneNumber);

	List<TUser> getUsersByDeviceId(int configDeviceId);
	
	int addUserObox(TUserObox tUserObox);

	/**  
	 * @param tUserDevice  
	 * @Description:  
	 */
	int addUserDevice(TUserDevice tUserDevice);

	/**  
	 * @param tUserScene  
	 * @Description:  
	 */
	void addUserScene(TUserScene tUserScene);

	/**  
	 * @param userId
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
	TUserScene getUserSceneByUserIdAndSceneNumber(Integer userId, Integer sceneNumber);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> getListOfUserDeviceByUserId(Integer userId);
}
