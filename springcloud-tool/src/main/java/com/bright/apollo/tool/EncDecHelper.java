package com.bright.apollo.tool;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.springframework.stereotype.Component;

@Component
public class EncDecHelper {

    public interface LgetLib extends Library {
        //LgetLib INSTANCE = (LgetLib) Native.loadLibrary("/Users/DO/Desktop/test/libOB.so",LgetLib.class);
        LgetLib INSTANCE = (LgetLib) Native.loadLibrary("/usr/local/lib/libOB.so",LgetLib.class);
        int OB_DATA_ENC_DEC(byte []data,byte []Random,byte []controlPW,int mode,int pattern);
		int AES_DATA_ENC_DEC(byte []data,byte []Random,int mode);
    }

    public int OB_DATA_ENC_DEC(byte []data,byte []Random,byte []controlPW,int mode,int pattern){
		return LgetLib.INSTANCE.OB_DATA_ENC_DEC(data,Random,controlPW,mode,pattern);
	}
	//date length 64,random 16,mode 0 decode /1
	public int AES_DATA_ENC_DEC(byte []data,byte []Random,int mode){
		return LgetLib.INSTANCE.AES_DATA_ENC_DEC(data,Random,mode);
	}
}
