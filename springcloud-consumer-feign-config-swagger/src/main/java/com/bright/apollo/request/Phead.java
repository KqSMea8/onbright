package com.bright.apollo.request;

import com.zz.common.exception.AppException;
import com.zz.common.util.CommonUtil;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月29日  
 *@Version:1.1.0  
 */
public class Phead {

	private int pversion;// 协议版本号
	private String aid;// 手机androidid
	private String imei;// 手机imei
	private String uid;// 用户id
	private int cid;// 产品id
	private int cversion;// 客户端软件版本num
	private String cversionname;// 客户端软件版名称
	private int channel;// 渠道号
	private String local;// 国家(大写)
	private String lang;// 语言（小写）
	private String imsi;// 运营商编码
	private String dpi;// 手机分辨率 宽*高；
	private int screenWidth = -1;
	private int screenHeight = -1;
	private int sdk;// 系统sdklevel
	private String sys;// 系统版本
	private String model;// 机型
	private String requesttime;// 请求时间，客户端请求服务器的手机时间格式：yyyy-MM-dd HH:mm:ss
	private int official;// 是否为官方 默认：0，1：官方，2：非官方
	private int hasmarket;// 是否安装了电子市场
	private String net;// 网络类型
	private String coordinates;// 经纬度信息:经度#纬度
	private String positions;// 定位到的位置信息:国家#省份#城市
	private int sbuy;// 是否支持内购 0：不支持，1：支持
	private String gadid;// google广告id
	private String cip;// 客户端ip

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("pversion=");
		builder.append(pversion);
		builder.append(", aid=");
		builder.append(aid);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", uid=");
		builder.append(uid);
		builder.append(", cid=");
		builder.append(cid);
		builder.append(", cversion=");
		builder.append(cversion);
		builder.append(", cversionname=");
		builder.append(cversionname);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", local=");
		builder.append(local);
		builder.append(", lang=");
		builder.append(lang);
		builder.append(", imsi=");
		builder.append(imsi);
		builder.append(", dpi=");
		builder.append(dpi);
		builder.append(", screenWidth=");
		builder.append(screenWidth);
		builder.append(", screenHeight=");
		builder.append(screenHeight);
		builder.append(", sdk=");
		builder.append(sdk);
		builder.append(", sys=");
		builder.append(sys);
		builder.append(", model=");
		builder.append(model);
		builder.append(", requesttime=");
		builder.append(requesttime);
		builder.append(", official=");
		builder.append(official);
		builder.append(", hasmarket=");
		builder.append(hasmarket);
		builder.append(", net=");
		builder.append(net);
		builder.append(", coordinates=");
		builder.append(coordinates);
		builder.append(", positions=");
		builder.append(positions);
		builder.append(", sbuy=");
		builder.append(sbuy);
		builder.append(", gadid=");
		builder.append(gadid);
		builder.append(", cip=");
		builder.append(cip);
		return builder.toString();
	}

	public int getPversion() {
		return pversion;
	}

	public void setPversion(int pversion) {
		this.pversion = pversion;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCversion() {
		return cversion;
	}

	public void setCversion(int cversion) {
		this.cversion = cversion;
	}

	public String getCversionname() {
		return cversionname;
	}

	public void setCversionname(String cversionname) {
		this.cversionname = cversionname;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		if (!CommonUtil.isNull(local)) {
			local = local.toUpperCase();
		}
		this.local = local;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getDpi() {
		return dpi;
	}

	public void setDpi(String dpi) {
		this.dpi = dpi;
	}

	public int getScreenWidth() {
		if (screenWidth < 0) {
			if (!CommonUtil.isNull(dpi) && (dpi.indexOf("*") > 0)) {
				String value = dpi.substring(0, dpi.indexOf("*"));
				if (CommonUtil.isNumber(value)) {
					screenWidth = Integer.valueOf(value).intValue();
				}
			}
			if (screenWidth < 0) {
				screenWidth = 0;
			}
		}
		return screenWidth;
	}

	public int getScreenHeight() {
		if (screenHeight < 0) {
			if (!CommonUtil.isNull(dpi) && (dpi.indexOf("*") > 0)) {
				String value = dpi.substring(dpi.indexOf("*") + 1);
				if (CommonUtil.isNumber(value)) {
					screenHeight = Integer.valueOf(value).intValue();
				}
			}
			if (screenHeight < 0) {
				screenHeight = 0;
			}
		}
		return screenHeight;
	}

	public int getSdk() {
		return sdk;
	}

	public void setSdk(int sdk) {
		this.sdk = sdk;
	}

	public String getSys() {
		return sys;
	}

	public void setSys(String sys) {
		this.sys = sys;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(String requesttime) {
		this.requesttime = requesttime;
	}

	public int getOfficial() {
		return official;
	}

	public void setOfficial(int official) {
		this.official = official;
	}

	public int getHasmarket() {
		return hasmarket;
	}

	public void setHasmarket(int hasmarket) {
		this.hasmarket = hasmarket;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public int getSbuy() {
		return sbuy;
	}

	public void setSbuy(int sbuy) {
		this.sbuy = sbuy;
	}

	public String getGadid() {
		return gadid;
	}

	public void setGadid(String gadid) {
		this.gadid = gadid;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public void validate() throws AppException {
		if (pversion <= 0) {
			throw new AppException(-1, "Phead.pversion is null");
		}
		if (CommonUtil.isNull(aid)) {
			throw new AppException(-1, "Phead.aid is null");
		}
//		if (CommonUtil.isNull(goid)) {
//			throw new AppException(-1, "Phead.goid is null");
//		}
		if (cid <= 0) {
			throw new AppException(-1, "Phead.cid is null");
		}
		if (cversion <= 0) {
			throw new AppException(-1, "Phead.cversion is null");
		}
		if (CommonUtil.isNull(cversionname)) {
			throw new AppException(-1, "Phead.cversionname is null");
		}
		if (channel <= 0) {
			throw new AppException(-1, "Phead.channel is null");
		}
		if (CommonUtil.isNull(local)) {
			throw new AppException(-1, "Phead.local is null");
		}
		local = local.toUpperCase();
		if (CommonUtil.isNull(lang)) {
			throw new AppException(-1, "Phead.lang is null");
		}
		if(lang.indexOf("_") > 0){
			lang = lang.split("_")[0];
		}
		lang = lang.toLowerCase();
		if (CommonUtil.isNull(imsi)) {
			throw new AppException(-1, "Phead.imsi is null");
		}
		if (CommonUtil.isNull(dpi)) {
			throw new AppException(-1, "Phead.dpi is null");
		}
		if (sdk <= 0) {
			throw new AppException(-1, "Phead.sdk is null");
		}
		if (CommonUtil.isNull(sys)) {
			throw new AppException(-1, "Phead.sys is null");
		}
		if (CommonUtil.isNull(model)) {
			throw new AppException(-1, "Phead.model is null");
		}
		if (CommonUtil.isNull(requesttime)) {
			throw new AppException(-1, "Phead.requesttime is null");
		}
		if (CommonUtil.isNull(gadid)) {
			throw new AppException(-1, "Phead.gadid is null");
		}
	}

}
