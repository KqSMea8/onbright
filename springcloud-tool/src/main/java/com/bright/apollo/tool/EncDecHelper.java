package com.bright.apollo.tool;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.springframework.stereotype.Component;

@Component
public class EncDecHelper {

    public interface LgetLib extends Library {
        //LgetLib INSTANCE = (LgetLib) Native.loadLibrary("/Users/DO/Desktop/test/libOB.so",LgetLib.class);
        LgetLib INSTANCE = (LgetLib) Native.loadLibrary("/usr/local/tomcat_7.0.39_bright-apollo/webproject/bright-apollo/WEB-INF/lib/libOB.so",LgetLib.class);
        int OB_DATA_ENC_DEC(byte []data,byte []Random,byte []controlPW,int mode,int pattern);
    }

    public int OB_DATA_ENC_DEC(byte []data,byte []Random,byte []controlPW,int mode,int pattern){
        return LgetLib.INSTANCE.OB_DATA_ENC_DEC(data,Random,controlPW,mode,pattern);
    }
}
