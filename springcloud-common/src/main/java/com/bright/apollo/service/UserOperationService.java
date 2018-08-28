package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TUserOperation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
public interface UserOperationService {

	/**  
	 * @param fromDate
	 * @param toDate
	 * @param serialId
	 * @param startIndex
	 * @param countIndex
	 * @return  
	 * @Description:  
	 */
	List<TUserOperation> getUserOperation(Long fromDate, Long toDate, String serialId, Integer startIndex,
			Integer countIndex);

	/**  
	 * @param tableName
	 * @param serialId
	 * @param day
	 * @return  
	 * @Description:  
	 */
	List<TUserOperation> queryUserOperationByMonth(String tableName, String serialId, String day);

	/**  
	 * @param tableName
	 * @return  
	 * @Description:  
	 */
	List<TUserOperation> queryUserOperationByMonthDayList(String tableName);

	/**  
	 * @param name
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TUserOperation> queryUserOperation(String name, String serialId);

	/**  
	 * @param tUserOperation  
	 * @Description:  
	 */
	int addUserOperation(TUserOperation tUserOperation);

	/**  
	 * @param from
	 * @param to
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TUserOperation> queryUserOperationByDate(long from, long to, String serialId);

}
