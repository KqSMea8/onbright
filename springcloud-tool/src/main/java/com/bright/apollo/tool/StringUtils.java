package com.bright.apollo.tool;

import com.bright.apollo.enums.CMDEnum;
import com.zz.common.util.ByteUtils;

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
}
