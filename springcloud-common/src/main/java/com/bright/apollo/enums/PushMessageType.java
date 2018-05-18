package com.bright.apollo.enums;

public enum PushMessageType {
    OBOX_TRANS((byte)0),
    SERVER_CHANGE((byte)1),
    OBOX_ONLINE((byte)2),

    ALERT((byte)3)//警报
    ;

    private byte value;

    private PushMessageType(byte value){
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
