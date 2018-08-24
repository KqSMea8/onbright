package com.bright.apollo.mqtt;

import com.bright.apollo.mqtt.bean.MqttProperties;
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

    @Autowired
    private MqttProperties mqttProperties;

    @Bean
    public MessageChannel outChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        String[] url = mqttProperties.getOutbound().getUrl().split(",");
        DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        defaultMqttPahoClientFactory.setServerURIs(url);
        defaultMqttPahoClientFactory.setCleanSession(false);
        defaultMqttPahoClientFactory.setUserName("admin");
        defaultMqttPahoClientFactory.setPassword("admin");
        return defaultMqttPahoClientFactory;
    }


    @Bean(name = "mqttPahoMessageHandler")
    @ServiceActivator(inputChannel = "outChannel")
    public MessageHandler mqttOutbound(){
        MqttPahoMessageHandler mqttPahoMessageHandler =
                new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(),mqttClientFactory());
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic("topic1");
        return mqttPahoMessageHandler;
    }


}
