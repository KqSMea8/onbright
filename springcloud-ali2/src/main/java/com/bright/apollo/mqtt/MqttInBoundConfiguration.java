package com.bright.apollo.mqtt;

import com.bright.apollo.mqtt.bean.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class MqttInBoundConfiguration {

    @Autowired
    private MqttProperties mqttProperties;

    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducerSupport inbound(){
        String[] topics = mqttProperties.getInbound().getTopics().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter
                = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getUrl(),mqttProperties.getInbound().getClientId(),topics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }



    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload());
            }

        };
    };

}
