package com.bright.apollo.tool;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zz.common.util.ByteUtils;

 
public class ByteHelper {

	/**
	 * 字符串转为Ascill byte数组
	 * 
	 * @param value
	 * @param size
	 * @return
	 */
	public static byte[] stringToAsciiBytes(String value, int size) {
		char[] chars = value.toCharArray();
		byte[] bs = new byte[size];
		for (int i = 0; i < chars.length; i++) {
			int v = (int) chars[i];
			bs[i] = (byte) v;
		}
		return bs;
	}

	// bit字符串 转换byte 字符串
	public static String bitsString2hexSString(String str) {
		StringBuilder sb = new StringBuilder();
		if (org.springframework.util.StringUtils.isEmpty(str) || str.length() % 4 != 0)
			return null;
		for (int i = 0; i < str.length(); i = i + 4) {
			String substring = str.substring(i, i + 4);
			String bits2hexString = bits2hexString(substring);
			if (org.springframework.util.StringUtils.isEmpty(bits2hexString))
				return null;
			sb.append(bits2hexString);
		}
		return sb.toString();

	}

	public static String bits2hexString(String str) {
		int length = 0;
		if (org.springframework.util.StringUtils.isEmpty(str) || str.length() != 4)
			return null;
		try {
			for (int i = 0; i < str.length(); i++) {
				length = length
						+ (int) (Integer.parseInt(str.substring(i, i + 1)) * Math
								.pow(2, 3 - i));
			}
			if (length > 16)
				return null;
			return Integer.toHexString(length);
		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * 获取字节数据的去符号位值
	 */
	public static int validByte(byte src) {
		return src & 0xff;
	}

	// 处理高位字节
	public static int byteIndexValid(byte src, int startPos, int len) {
		if (startPos + len > 8) {
			throw new IndexOutOfBoundsException();
		}
		int srcInt = src & 0xff;
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += ((srcInt >> (startPos + i)) & 0x01) << i;
		}
		return sum;
	}

	public static byte[] stringToByte(String value, int size) {
		byte[] bs = new byte[size];
		byte[] valueByte = value.getBytes();
		System.arraycopy(valueByte, 0, bs, 1, value.length());
		return bs;
	}

	/**
	 * 16进制字符串转为byte数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexStringToBytes(String str) {
		if (str.length() % 2 != 0) {
			throw new NumberFormatException(
					"Hex String must be exactly two digits per byte.");
		}
		byte[] bs = new byte[str.length() / 2];
		for (int i = 0; i < bs.length; i++) {
			String x = str.substring(i * 2, i * 2 + 2);
			int m = Integer.parseInt(x, 16);
			bs[i] = (byte) m;
		}
		return bs;
	}

	/**
	 * byte数组转为十六进制字符串
	 *
	 * @return 十六进制字符串
	 */
	public static String byteArryToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (byte aSrc : src) {
			int v = aSrc & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}

	public static byte[] int2byte2(int res) {
		byte[] targets = new byte[2];

		targets[1] = (byte) (res & 0xff);// 最低位
		targets[0] = (byte) ((res >> 8) & 0xff);// 次低位

		return targets;
	}

	/**
	 * byte数组转为16进制字符串
	 * 
	 * @param src
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return stringBuilder.toString();
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hv = 0 + hv;
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String int2HexString(int s) {
		String sv = Integer.toHexString(s).toString();
		if (sv.length() == 1) {
			sv = "0" + sv;
		}
		return sv;
	}

	/**
	 * 将byte数组转为16进制，再转为acill字符串
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	// public static String toHexAscii(byte [] bs) {
	// return ByteUtils.toHexAscii(bs, "");
	// }

	/**
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String fromHexAscii(String s)
			throws UnsupportedEncodingException, Exception {
		return new String(ByteUtils.fromHexAscii(s), "UTF-8").trim();
	}

	public static int char2ASCII(char c) {
		return (int) c;
	}

	public static int[] string2ASCII(String s) {// 字符串转换为ASCII码
		if (s == null || "".equals(s)) {
			return null;
		}

		char[] chars = s.toCharArray();
		int[] asciiArray = new int[chars.length];

		for (int i = 0; i < chars.length; i++) {
			asciiArray[i] = char2ASCII(chars[i]);
		}
		return asciiArray;
	}

	public static String timeString2Cron(String timeString) {
		String cron = "0 ";
		String cycleString = timeString.substring(0, 2);
		String year = timeString.substring(4, 6);
		String month = timeString.substring(6, 8);
		String day = timeString.substring(8, 10);
		String hour = timeString.substring(10, 12);
		String min = timeString.substring(12, 14);

		cron = cron + String.format("%d ", Integer.parseInt(min, 16))
				+ String.format("%d ", Integer.parseInt(hour, 16));
		int cycle = Integer.parseInt(cycleString, 16);
		if (cycle == 0) {
			cron = cron + String.format("%d ", Integer.parseInt(day, 16))
					+ String.format("%d ", Integer.parseInt(month, 16)) + "? "
					+ String.format("20%d ", Integer.parseInt(year, 16));
		} else {
			if ((cycle & 0x80) == 0x80) {
				cron = cron + "* * ?";
			} else {
				cron = cron + "? * ";
				for (int i = 0; i < 7; i++) {
					if (((cycle >> i) & 0x01) == 0x01) {
						if (i == 0) {
							cron = cron + String.format("%d", i + 1);
						} else {
							if (cron.length() == 10) {
								cron = cron + String.format("%d", i + 1);
							} else {
								cron = cron + String.format(",%d", i + 1);
							}
						}
					}
				}
			}
		}
		return cron;

	}
	//根据timeString count轮询次数 interval间隔时间
	public static String[] timeString2Cron(String timeString, int count,
			int interval) {
		String timeString2Cron = timeString2Cron(timeString);
		String[] array=null;
		String[] split = timeString2Cron.split(" ");
		if(split.length>0){
			StringBuilder sb=new StringBuilder();
			int min = Integer.parseInt(split[1]);
			int hour = Integer.parseInt(split[2]);
			int minTemp,hourTemp;
			if(split[split.length-1].equals("*")||split[split.length-1].equals("?")){
				array=new String[count+1];
				array[0]=timeString2Cron;
				for (int i = 1; i <=count; i++) {
					minTemp=i*interval+min;
					if(min<60){
						split[1]=minTemp+"";
						for (int j = 0; j < split.length; j++) {
							sb.append(split[j]).append(" ");
						}
						array[i]=sb.toString();
					}else{
						split[1]=minTemp%60+"";
						hourTemp=minTemp/60+hour;
						if(hourTemp>=24){
							hourTemp=hourTemp%24;
						}
						split[2]=hourTemp+"";
						for (int j = 0; j < split.length; j++) {
							sb.append(split[j]).append(" ");
						}
						array[i]=sb.toString();
					}
					sb.delete( 0, sb.length() );
				}
			}else{
				String[] week = split[split.length-1].split(",");
				array=new String[(count+1)*week.length];
				//初始化
				for (int i = 0; i < week.length; i++) {
					for (int j = 0; j < split.length-1; j++) {
						sb.append(split[j]).append(" ");
					}
					array[i]=sb.append(week[i]).toString();
				}
				StringBuilder sbTemp=new StringBuilder();
				for (int i =1; i <=count; i++) {
					minTemp=i*interval+min;
					if(minTemp<60){
						split[1]=minTemp+"";
						for (int j = 0; j < split.length-1; j++) {
							sb.append(split[j]).append(" ");
						}
						for (int j = 0; j < week.length; j++) {
							sbTemp.append(sb);
							sbTemp.append(week[j]);
							array[i*week.length+j]=sbTemp.toString();
							sbTemp.delete( 0, sbTemp.length() );
						}
					}else{
						split[1]=minTemp%60+"";
						hourTemp=minTemp/60+hour;
						if(hourTemp>=24){
							int addDay=hourTemp/24;
							hourTemp=hourTemp%24;
							split[2]=hourTemp+"";
							for (int j = 0; j < split.length-1; j++) {
								sb.append(split[j]).append(" ");
							}
							for (int j = 0; j < week.length; j++) {
								sbTemp.append(sb);
								int day=Integer.parseInt(week[j])+addDay;
								if(day>7){
									day=day%7;
								}
								sbTemp.append(day);
								array[i*week.length+j]=sbTemp.toString();
								sbTemp.delete( 0, sbTemp.length() );
							}
						}else{
							split[2]=hourTemp+"";
							for (int j = 0; j < split.length-1; j++) {
								sb.append(split[j]).append(" ");
							}
							for (int j = 0; j < week.length; j++) {
								sbTemp.append(sb);
								sbTemp.append(week[j]);
								array[i*week.length+j]=sbTemp.toString();
								sbTemp.delete( 0, sbTemp.length() );
							}
						}
						
					}
					sb.delete( 0, sb.length() );
				}
			}
			//去重
			List list = Arrays.asList(array);
			Set set = new HashSet(list);
			return (String [])set.toArray(new String[0]);
		}
		return array;
	}

	public static String timeString2Cron(long startTime, int count, int interval) {
		// 0 1,2,3 11,12 * * ? : 每天11:01,11:02,11:03; 12:01,12:02,12:03分执行任务

		StringBuilder sbM = new StringBuilder();// 分
		StringBuilder sbH = new StringBuilder();// 分
		// yyyy-MM-dd HH:mm:ss
		for (int i = 0; i < count; i++) {
			String time = DateHelper.formatDate(startTime + i * 60 * 1000
					* interval, DateHelper.FORMATALL);
			String[] split = time.split(" ");
			if (split.length == 2) {
				String[] split2 = split[1].split(":");
				if (split2.length == 3) {
					sbH.append(split2[0] + ",");
					sbM.append(split2[1] + ",");
				}
			}
		}
		return "0 " + sbM.toString().substring(0, sbM.toString().length() - 1)
				+ " "
				+ sbH.toString().substring(0, sbM.toString().length() - 1)
				+ "  * * ?";
	}

	public static void main(String[] args) throws Exception {
		/*String[] array=["1","2","3"];
		List list = Arrays.asList(array);
		Set set = new HashSet(list);

		String [] rid=(String [])set.toArray(new String[0]);*/
		byte[] bytes = hexStringToBytes("070aba044d020c01261512153Bffff02A8A4B24D1FE0847Bffff0415C4A7A4B24D1FE0847Bffff0215CD");
		String str = new String(bytes);
		System.out.println(str);
	}

	/**  
	 * @param b
	 * @return  
	 * @Description:  
	 */
	public static String byteToBit(byte b) {  
        return ""  
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    }

}
