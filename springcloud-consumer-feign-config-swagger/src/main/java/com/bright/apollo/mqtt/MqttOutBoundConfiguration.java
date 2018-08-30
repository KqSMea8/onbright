package com.bright.apollo.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttOutBoundConfiguration {

//    @Autowired
//    private MqttProperties mqttProperties;

    @Bean
    public MessageChannel outChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        defaultMqttPahoClientFactory.setServerURIs("tcp://127.0.0.1:1883");
        defaultMqttPahoClientFactory.setCleanSession(false);
        defaultMqttPahoClientFactory.setUserName("admin");
        defaultMqttPahoClientFactory.setPassword("admin");
        return defaultMqttPahoClientFactory;
    }


    @Bean(name = "mqttPahoMessageHandler")
    @ServiceActivator(inputChannel = "outChannel")
    public MessageHandler mqttOutbound(){
        MqttPahoMessageHandler mqttPahoMessageHandler =
                new MqttPahoMessageHandler("aliOutbound",mqttClientFactory());
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic("topic1");
        return mqttPahoMessageHandler;
    }


}
