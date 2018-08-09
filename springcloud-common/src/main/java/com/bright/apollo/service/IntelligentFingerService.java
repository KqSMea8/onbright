package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
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

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	Integer queryCountIntelligentWarnBySerialId(String serialId);

	/**  
	 * @param serialId
	 * @param end
	 * @param start
	 * @return  
	 * @Description:  
	 */
	List<IntelligentFingerWarnDTO> queryIntelligentWarnByDate(String serialId, String end, String start);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	Integer queryCountIntelligentUserBySerialId(String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TIntelligentFingerUser> queryIntelligentUserBySerialId(String serialId);

	/**  
	 * @param serialId
	 * @param pin
	 * @return  
	 * @Description:  
	 */
	TIntelligentFingerUser queryIntelligentFingerUserBySerialIdAndPin(String serialId, String pin);

	/**  
	 * @param fingerUser  
	 * @Description:  
	 */
	void updatentelligentFingerUser(TIntelligentFingerUser fingerUser);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	TIntelligentFingerAuth queryIntelligentAuthBySerialId(String serialId);

	/**  
	 * @param auth  
	 * @Description:  
	 */
	int addIntelligentFingerAuth(TIntelligentFingerAuth auth);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TIntelligentFingerRemoteUser> queryIntelligentFingerRemoteUsersBySerialId(String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TIntelligentFingerAbandonRemoteUser> queryTIntelligentFingerAbandonRemoteUsersBySerialId(String serialId);

	/**  
	 * @param id  
	 * @Description:  
	 */
	void delIntelligentFingerAbandonRemoteUserById(Integer id);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TIntelligentFingerRemoteUser> queryTIntelligentFingerRemoteUsersBySerialId(String serialId);

	/**  
	 * @param fingerRemoteUser
	 * @return  
	 * @Description:  
	 */
	Integer addTIntelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	TIntelligentFingerRemoteUser queryTintelligentFingerRemoteUserById(int id);

	/**  
	 * @param fingerRemoteUser  
	 * @Description:  
	 */
	void updateTintelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser);

	/**  
	 * @param abandonRemoteUser  
	 * @Description:  
	 */
	Integer addIntelligentFingerAbandonRemoteUser(TIntelligentFingerAbandonRemoteUser abandonRemoteUser);

	/**  
	 * @param id  
	 * @Description:  
	 */
	void delTIntelligentFingerRemoteUserById(int id);

	/**  
	 * @param fingerAuth  
	 * @Description:  
	 */
	void updateTintelligentFingerAuth(TIntelligentFingerAuth fingerAuth);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	List<TIntelligentFingerPush> queryTIntelligentFingerPushsBySerialId(String serialId);

	/**  
	 * @param serialId
	 * @param pin
	 * @return  
	 * @Description:  
	 */
	TIntelligentFingerRemoteUser queryTIntelligentFingerRemoteUserBySerialIdAndPin(String serialId, int pin);

	/**  
	 * @param mobile
	 * @param serialId  
	 * @Description:  
	 */
	void updateTIntelligentFingerPushMobileBySerialId(String mobile, String serialId);

	/**  
	 * @param enable
	 * @param serialId
	 * @param value  
	 * @Description:  
	 */
	void updateTIntelligentFingerPushEnableBySerialIdAndValue(Integer enable, String serialId, Integer value);

 

 
 
}
