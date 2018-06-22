package com.bright.apollo.transition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
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
