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

    private String data;

    public OboxResp(Type type) {
        Assert.notNull(type, "type can't be null!");
        Assert.isTrue(type != Type.success, "when type is success, data can't be null");
        this.type = type;
    }

    public OboxResp(Type type, String data) {
        Assert.notNull(type, "type can't be null!");
        Assert.notNull(data, "data can't be null!");
        this.type = type;
        this.data = data;

    }

    public Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "OboxResp [type=" + type + ", data=" + data + "]";
    }
}
