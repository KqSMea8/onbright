package com.bright.apollo.enums;

public enum RokidSwitchEnum {

    on("on"),
    off("off");

    private String value;

    private RokidSwitchEnum(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
