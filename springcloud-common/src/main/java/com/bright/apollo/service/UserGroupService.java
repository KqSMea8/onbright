package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TUSerGroup;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
public interface UserGroupService {

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TUSerGroup> queryUserGroup(Integer userId);

}
