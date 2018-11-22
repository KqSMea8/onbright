package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TServerGroup;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
public interface ServerGroupService {

	/**  
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	TServerGroup querySererGroupById(Integer groupId);

	/**  
	 * @param string
	 * @return  
	 * @Description:  
	 */
	List<TServerGroup> queryServerGroupByAddr(String addr);

	/**  
	 * @param tServerGroup  
	 * @Description:  
	 */
	int addServerGroup(TServerGroup tServerGroup);

	/**  
	 * @param tServerGroup  
	 * @Description:  
	 */
	void updateServerGroup(TServerGroup tServerGroup);

	/**  
	 * @param groupId  
	 * @Description:  
	 */
	int deleteServerGroup(Integer groupId);

	/**  
	 * @param userId
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	TServerGroup queryGroupByUserAndGroup(Integer userId, Integer groupId);

}
