package com.bright.apollo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IndexUtils {
    public static Integer getIdx(){
        Calendar c=Calendar.getInstance();
        String time=new SimpleDateFormat("yyyy-MM-ddHHmmss").format(c.getTime()).toString();
        System.out.println(time);
        StringBuffer s=new StringBuffer(time.substring(14, 16));
        Long sys=System.currentTimeMillis();
        s.append(sys.toString().substring(10, 13));
        Double tm=Math.random()*10000+1;
        s.append(tm.toString().substring(tm.toString().length()-4, tm.toString().length()));
        return Integer.valueOf(s.toString());
    }
}
