package com.apress.spring_quick.springint;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway
public interface KafkaGateway {

    @Gateway(requestChannel = "toKafka.input")
    void sendToKafka(String payload, @Header(KafkaHeaders.TOPIC) String topic);

    @Gateway(replyChannel = "fromKafka", replyTimeout = 10000)
    Message<?> receiveFromKafka();
}
