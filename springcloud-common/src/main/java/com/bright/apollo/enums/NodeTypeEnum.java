package com.bright.apollo.enums;

public enum NodeTypeEnum {
    // 节点/组/摄像头/场景使能/nvr 00/01/02/03/04/05
    single("00"),

    group("01"),

    camera("02"),

    status("03"),

    nvr("04"),

    security("05"),//监听
    
    wifi("06")
    ;

    private String value;

    private NodeTypeEnum(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
