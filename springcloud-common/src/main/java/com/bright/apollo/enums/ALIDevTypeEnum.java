package com.bright.apollo.enums;

public enum ALIDevTypeEnum {
    OBOX("a103Rjc0j6B","3PQo9W8wweT"),

    DEVICE("b1eakXpQ1WU","LJjxCM3gZrj")
    ;

    private String southChinaName;
    private String americaName;

    private ALIDevTypeEnum() {

    }

    private ALIDevTypeEnum(String southChinaName,String americaName) {
        this.southChinaName = southChinaName;
        this.americaName = americaName;
    }

    public String getSouthChinaName() {
        return this.southChinaName;
    }

    public void setSouthChinaName(String southChinaName) {
        this.southChinaName = southChinaName;
    }

    public String getAmericaName() {
        return this.americaName;
    }

    public void setAmericaName(String americaName) {
        this.americaName = americaName;
    }

    public static ALIDevTypeEnum getType(String devType) {
        for (ALIDevTypeEnum typeEnum : ALIDevTypeEnum.values()) {
            if (typeEnum.name().equals(devType)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static ALIDevTypeEnum getTypebyValue(String devName) {
        for (ALIDevTypeEnum typeEnum : ALIDevTypeEnum.values()) {
            if (typeEnum.getAmericaName().equals(devName) ||
                    typeEnum.getSouthChinaName().equals(devName)) {
                return typeEnum;
            }
        }
        return null;
    }
}
