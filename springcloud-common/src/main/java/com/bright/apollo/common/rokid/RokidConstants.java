package com.bright.apollo.common.rokid;

public interface RokidConstants {
    //for switch  enum
    public final static String[] SWITCH={"on","off"};
    //for light
    public final static String[] BRINTNESS={"up","down","max","min","num"};
    public final static String[] COLORTEMPERATURE={"up","down","max","min","num"};
    //for action property
    public final static String[] PROPERTY={"switch","brightness","color_temperature"};
    //propertyString
    public final static String BRIGHTNESSTYPE="brightness";
    public final static String COLORTEMPERATURETYPE="color_temperature";

    //typeString
    public final static String SWITCHTYPE="switch";
    public final static String LIGHT="light";

    //errorMessage
    public final static String UNKONWDEVICE="UnKonw device";
    public final static String ERRORSTATE="error state";

    //errorName
    public final static String E_DRIVER_ERROR="E_DRIVER_ERROR";
    public final static String E_DRIVER_SIGN_ERROR="E_DRIVER_SIGN_ERROR";
    public final static String E_DRIVER_DEVICE_NO_FOUND="E_DRIVER_DEVICE_NO_FOUND";
    public final static String E_DRIVER_TIMEOUT="E_DRIVER_TIMEOUT";
    public final static String E_DRIVER_LOGIN_FAILED="E_DRIVER_LOGIN_FAILED";
    public final static String E_DRIVER_WRONG_USERNAME_PASSWORD="E_DRIVER_WRONG_USERNAME_PASSWORD";
}
