package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TMsg;
import com.bright.apollo.response.MsgExceptionDTO;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
public interface TMsgService {
	
	/**  
	 * @param tMsg  
	 * @Description:  
	 */
	void addMsg(TMsg tMsg);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	Integer countMsgList(Integer userId);

	/**  
	 * @param userId
	 * @param type
	 * @param start
	 * @param count
	 * @return  
	 * @Description:  
	 */
	List<MsgExceptionDTO> queryMsgExceList(Integer userId, Integer type, int start, int count);

	/**  
	 * @param userId
	 * @param type
	 * @return  
	 * @Description:  
	 */
	Integer countMsgExceList(Integer userId, Integer type);

	/**  
	 * @param id
	 * @param statue  
	 * @Description:  
	 */
	void updateMsgState(Integer id, int statue);

}
