package com.bright.apollo.enums;

public enum SceneTypeEnum {
    server("00"),

    local("01"),

    someone("02"),

    onone("03"),

    defend("04");

    private String value;

    private SceneTypeEnum(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
