package com.bright.apollo.request;

import java.io.Serializable;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月24日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdInfo implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 7364564163717561756L;
	@JsonProperty(value="obox")
	private TObox obox;
	@JsonProperty(value="cmd")
	private CMDEnum cmd;
	@JsonProperty(value="bytes")
	private byte[] bytes;

	public TObox getObox() {
		return obox;
	}

	public void setObox(TObox obox) {
		this.obox = obox;
	}

	public CMDEnum getCmd() {
		return cmd;
	}

	public void setCmd(CMDEnum cmd) {
		this.cmd = cmd;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**  
	 * @param obox
	 * @param cmd
	 * @param bytes  
	 */
	public CmdInfo(TObox obox, CMDEnum cmd, byte[] bytes) {
		super();
		this.obox = obox;
		this.cmd = cmd;
		this.bytes = bytes;
	}

	/**  
	 *   
	 */
	public CmdInfo() {
		super();
	}
	
	

}
