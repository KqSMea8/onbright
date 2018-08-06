package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.request.IntelligentOpenRecordDTO;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
public interface IntelligentFingerService {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	Integer countFingerAuth(String serialId);

	/**  
	 * @param serialId
	 * @param end
	 * @param start
	 * @return  
	 * @Description:  
	 */
	List<IntelligentOpenRecordDTO> queryIntelligentOpenRecordByDate(String serialId, String end, String start);

}
