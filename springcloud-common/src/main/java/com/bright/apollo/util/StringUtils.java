package com.bright.apollo.util;

import java.io.UnsupportedEncodingException;

import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.ByteUtils;

 

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
public class StringUtils {

    public static String bytes2String(CMDEnum cmd, byte [] data,int packageLength,String head){
        byte [] out = new byte [packageLength];
        out[0] = 0x00;
        out[1] = 0x00;
        out[2] = 0x00;
        out[3] = 0x00;
        // 命令2个字节
        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
        System.arraycopy(cmdBytes, 0, out, 4, cmdBytes.length);

        // 真实数据长度
        out[6] = (byte)data.length;
        // 真实数据
        System.arraycopy(data, 0, out, 7, data.length);
        // 结束符
        out[61] = (byte)Integer.parseInt("55", 16);
        // 计算校验和
        int sum = 0;
        for (int i = 0; i < 62; i ++) {
            int v = out[i] & 0xFF;
            sum += v;
        }
        byte [] sumBytes = ByteUtils.convertInt2Bytes(sum);
        out [62] = sumBytes[2];
        out [63] = sumBytes[3];

        return head + ByteHelper.bytesToHexString(out);
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
    	byte[] bodyBytes = new byte[21];
		bodyBytes[0] = 0x01;
		bodyBytes[1] = 0x11;
		bodyBytes[2] = 0x00;
		String newName = ByteHelper.bytesToHexString("234657".getBytes("UTF-8"));
		byte[] namebytes = ByteHelper.hexStringToBytes(newName);
		System.arraycopy(namebytes, 0, bodyBytes, 3, namebytes.length);

 			byte[] groupbytes = ByteHelper.hexStringToBytes("00");
			System.arraycopy(groupbytes, 0, bodyBytes, 19, groupbytes.length);
     	bytes2String(CMDEnum.setting_sc_info, bodyBytes, 64, "38383838");
	}

}
