package com.bright.apollo.transition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


@Configuration
@RefreshScope
public class TMallTemplate {

    @Value("${tmallactions.lightactions}")
    private String lightActions;

    @Value("${tmallactions.defaultactions}")
    private String defaultAction;

    @Value("${tmallproperties.defaultproperties}")
    private String defaultProperties;

    @Value("${tmallproperties.lightproperties}")
    private String lightProperties;

    @Value("${tmall.lights}")
    private String lightsDevices;

    @Value("${tmall.switchs}")
    private String switchDevices;

    @Value("${tmall.curtains}")
    private String curtainDevices;

    @Value("${tmallControllProperties.light}")
    private String lightControllProperties;


    public String getTvProperties() {
        return tvProperties;
    }

    public void setTvProperties(String tvProperties) {
        this.tvProperties = tvProperties;
    }

    public String getAirconditionProperties() {
        return airconditionProperties;
    }

    public void setAirconditionProperties(String airconditionProperties) {
        this.airconditionProperties = airconditionProperties;
    }

    @Value("${tmallControllProperties.tv}")
    private String tvProperties;

    @Value("${tmallControllProperties.aircondition}")
    private String airconditionProperties;

    public String getLightControllProperties() {
        return lightControllProperties;
    }

    public void setLightControllProperties(String lightControllProperties) {
        this.lightControllProperties = lightControllProperties;
    }

    public String getDefaultControllProperties() {
        return defaultControllProperties;
    }

    public void setDefaultControllProperties(String defaultControllProperties) {
        this.defaultControllProperties = defaultControllProperties;
    }

    @Value("${tmallControllProperties.outletAndcurtain}")
    private String defaultControllProperties;

    public String getLightActions() {
        return lightActions;
    }

    public void setLightActions(String lightActions) {
        this.lightActions = lightActions;
    }

    public String getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(String defaultAction) {
        this.defaultAction = defaultAction;
    }

    public String getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(String defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public String getLightProperties() {
        return lightProperties;
    }

    public void setLightProperties(String lightProperties) {
        this.lightProperties = lightProperties;
    }

    public String getLightsDevices() {
        return lightsDevices;
    }

    public void setLightsDevices(String lightsDevices) {
        this.lightsDevices = lightsDevices;
    }

    public String getSwitchDevices() {
        return switchDevices;
    }

    public void setSwitchDevices(String switchDevices) {
        this.switchDevices = switchDevices;
    }

    public String getCurtainDevices() {
        return curtainDevices;
    }

    public void setCurtainDevices(String curtainDevices) {
        this.curtainDevices = curtainDevices;
    }
}
