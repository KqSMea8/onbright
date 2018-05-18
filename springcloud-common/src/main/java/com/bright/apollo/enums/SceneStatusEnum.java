package com.bright.apollo.enums;

public enum SceneStatusEnum {
    disable(0),

    enable(1);


    private int value;

    private SceneStatusEnum(int v) {
        this.value = v;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SceneStatusEnum getEnum(String status) {
        for (SceneStatusEnum typeEnum : SceneStatusEnum.values()) {
            if (typeEnum.name().equals(status)) {
                return typeEnum;
            }
        }
        return enable;
    }
}
