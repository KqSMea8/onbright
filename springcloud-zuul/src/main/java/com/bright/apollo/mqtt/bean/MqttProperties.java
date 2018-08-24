package com.bright.apollo.mqtt.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:bootstrap.yml")
@ConfigurationProperties(prefix = "com.mqtt")
public class MqttProperties {

    private MqttInboundProperties inbound;

    private MqttOutboundProperties outbound;

    public MqttInboundProperties getInbound() {
        return inbound;
    }

    public void setInbound(MqttInboundProperties inbound) {
        this.inbound = inbound;
    }

    public MqttOutboundProperties getOutbound() {
        return outbound;
    }

    public void setOutbound(MqttOutboundProperties outbound) {
        this.outbound = outbound;
    }
}
