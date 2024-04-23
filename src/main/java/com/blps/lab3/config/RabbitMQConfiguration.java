package com.blps.lab3.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

   @Bean
    public MqttClient mqttClient() throws MqttException {
        String broker = "tcp://localhost:1883";
        String clientId = "mqtt-client";
        MqttClient mqttClient = new MqttClient(broker, clientId);
        mqttClient.connect();
        return mqttClient;
    }


}
