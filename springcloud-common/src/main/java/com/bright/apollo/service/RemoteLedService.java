package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TRemoteLed;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月11日  
 *@Version:1.1.0  
 */
public interface RemoteLedService {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TRemoteLed> getListBySerialId(String serialId);

	/**  
	 * @param list  
	 * @Description:  
	 */
	void batchRemoteLeds(List<TRemoteLed> list);

	/**  
	 * @param updateList  
	 * @Description:  
	 */
	@Deprecated
	void batchUpdateRemotes(List<TRemoteLed> updateList);

	/**  
	 * @param remoteLed  
	 * @Description:  
	 */
	void updateRemote(TRemoteLed remoteLed);

}
