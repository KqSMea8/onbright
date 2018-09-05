package com.bright.apollo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.tool.Base64Util;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.DateHelper;
import com.bright.apollo.tool.EncDecHelper;
import com.bright.apollo.tool.NumberHelper;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
@Component
public class FingerUtil {
	@Autowired
	private EncDecHelper helper;
	private static final Logger logger = LoggerFactory.getLogger(FingerUtil.class);
	/**  
	 * @param type
	 * @param obox
	 * @param deviceConfig
	 * @param startTime
	 * @param endTime
	 * @param times
	 * @param userSerialId
	 * @param randomNum  
	 * @Description:  
	 */
	public byte[] buildBytes(String type,String serialId, String address,
			String startTime, String endTime, String times, Integer userSerialId, String randomNum){
		byte[] bodyBytes = new byte[32];
		byte[] oboxSerildIdBytes = ByteHelper.hexStringToBytes(serialId);
		System.arraycopy(oboxSerildIdBytes, 0, bodyBytes, 0, oboxSerildIdBytes.length);
		bodyBytes[5] = 0x00;
		bodyBytes[6] = (byte) Integer.parseInt(address, 16);
		byte[] setType = ByteHelper.hexStringToBytes(type);
		//设置类型
		System.arraycopy(setType, 0, bodyBytes, 7, setType.length);
		if(type.equals("04")){
			return bodyBytes;
		}
		byte[] msgType=ByteHelper.hexStringToBytes("01");
		//消息类型
		System.arraycopy(msgType, 0, bodyBytes, 8, msgType.length);
		bodyBytes[9] = (byte) Integer.parseInt(Integer.toHexString(userSerialId), 16);		
		// 加密字符串
		String pin=Integer.toHexString(userSerialId);
		if(Integer.toHexString(userSerialId).length()<=1){
			pin=0+Integer.toHexString(userSerialId);
		}
		byte[] pwdBytes = null;
		if(NumberHelper.isNumeric(randomNum))
			pwdBytes = ByteHelper.stringToAsciiBytes("62" + userSerialId+"" + randomNum, 16);
		else
			pwdBytes = ByteHelper.stringToAsciiBytes("62" + userSerialId+"" + Base64Util.base64Decrypt(randomNum), 16);
		byte[] hexStringToBytes =ByteHelper.stringToAsciiBytes("smart_doorlock", 64);
  		byte[] hexStringToBytes2 = ByteHelper.hexStringToBytes(pin);
 		byte[] hexStringToBytes3 = ByteHelper.hexStringToBytes(address);
 		System.arraycopy(hexStringToBytes2, 0, hexStringToBytes, "smart_doorlock".toCharArray().length, hexStringToBytes2.length);
 		System.arraycopy(hexStringToBytes3, 0, hexStringToBytes, "smart_doorlock".toCharArray().length+hexStringToBytes2.length, hexStringToBytes3.length);
 		
 		//out 64bytes ,pwdBytes 8bytes
		//AES_DATA_ENC_DEC
 		logger.info("随机数:"+ByteHelper.byteArryToHexString(pwdBytes));
 		logger.info("加密前:"+ByteHelper.byteArryToHexString(hexStringToBytes));
		helper.AES_DATA_ENC_DEC(hexStringToBytes, pwdBytes,0);		 
		logger.info("加密后:"+ByteHelper.byteArryToHexString(hexStringToBytes));
		System.arraycopy(hexStringToBytes, 0, bodyBytes, 10, 10);
		byte[] msgType2 = ByteHelper.hexStringToBytes("02");
		System.arraycopy(msgType2, 0, bodyBytes, 20, msgType2.length);
		bodyBytes[21] = (byte) Integer.parseInt(Integer.toHexString(userSerialId), 16);
		// 时间
		String startDate = DateHelper.formatDate(Long.parseLong(startTime), DateHelper.FORMATNALLWITHPOINT);
		byte[] startByte = ByteHelper.handleDate(startDate);
		System.arraycopy(startByte, 0, bodyBytes, 22, startByte.length);
		String endDate = DateHelper.formatDate(Long.parseLong(endTime), DateHelper.FORMATNALLWITHPOINT);
		byte[] endByte = ByteHelper.handleDate(endDate);
		System.arraycopy(endByte, 0, bodyBytes, 26, endByte.length);
		//byte[] timesByte = ByteHelper.int2byte(Integer.parseInt(times));
		String hexString = null;
		if(Integer.parseInt(times)>=0)
			hexString = Integer.toHexString(Integer.parseInt(times));
		else//无线次数
			hexString = Integer.toHexString(1);
		if(hexString.length()%2!=0){
			hexString="0"+hexString;
		}
		byte[] timesByte = ByteHelper.hexStringToBytes(hexString);
		if(timesByte.length==1){
			bodyBytes[30] = 0x00;
		}
		System.arraycopy(timesByte, 0, bodyBytes, 31, timesByte.length);		
		return bodyBytes;
	}
}
