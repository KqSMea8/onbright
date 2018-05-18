package com.bright.apollo.enums;

public enum DeviceTypeEnum {
    led("01"),

    led_signal("01"),

    led_double("02"),

    led_rgb("03"),

    cooker("02"),

    humidifier("03"),

    socket("04"),

    socket_scene("2b"),

    socket_jbox("01"),

    socket_signalLine("02"),

    socket_touch("03"),

    socket_3_key_touch("17"),


    curtain("05"),

    curtain_curtain("01"),

    curtain_projector("02"),

    fan("06"),

    cleaner("07"),

    obox("0a"),

    sensor("0b"),

    sensor_als("01"),

    sensor_leach("02"),

    sensor_radar("03"),

    sensor_co("04"),

    sensor_ir("06"),

    sensor_pm25("07"),

    sensor_luminous("0a"),

    sensor_temp_humidity("0b"),

    sensor_smoke("0c"),

    infrared_sensor("0e"),

    sensor_card("0f"),

    //环境传感器（6合1）
    sensor_environment("10"),

    sensor_gesture("11"),

    camera("11"),

    fingerprint("14") ,

    //门锁
    doorlock("15") ,
    //门锁子类
    doorlock_child("02")
    ;

    private String value;

    private DeviceTypeEnum(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
