package com.bright.apollo.common.dto;

import org.springframework.util.Assert;

public class OboxResp {
    public enum Type {
        success,

        reply_timeout,

        socket_write_error,

        obox_process_failure,
        
        not_online;
    }

    private Type type;

    private String date;
    public OboxResp() {}
    public OboxResp(Type type) {
        Assert.notNull(type, "type can't be null!");
        Assert.isTrue(type != Type.success, "when type is success, date can't be null");
        this.type = type;
    }

    public OboxResp(Type type, String date) {
        Assert.notNull(type, "type can't be null!");
        Assert.notNull(date, "date can't be null!");
        this.type = type;
        this.date = date;

    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

 

     
}
