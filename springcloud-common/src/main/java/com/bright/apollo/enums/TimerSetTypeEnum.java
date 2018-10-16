package com.bright.apollo.enums;

public enum TimerSetTypeEnum {
    add,

    delete,

    enable,

    disable;

    private TimerSetTypeEnum() {
        // TODO Auto-generated constructor stub
    }

    public static TimerSetTypeEnum getSetType(String name) {
        for (TimerSetTypeEnum typeEnum : TimerSetTypeEnum.values()) {
            if (typeEnum.name().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
