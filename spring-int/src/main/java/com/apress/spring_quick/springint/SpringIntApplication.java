package com.apress.spring_quick.springint;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.util.Map;

@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
public class SpringIntApplication {

    @Value("${kafka.topic}") private String topic;
    @Value("${kafka.topic2}") private String topic2;

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(SpringIntApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);
        context.getBean(SpringIntApplication.class).runDemo(context);
        context.close();
    }

    private void runDemo(ConfigurableApplicationContext context) {
        KafkaGateway kafkaGateway = context.getBean(KafkaGateway.class);
        System.out.println("Sending 10 messages...");
        for (int i = 0; i < 10; i++) {
            String message = "foo" + i;
            System.out.println("Send to Kafka: " + message);
            kafkaGateway.sendToKafka(message, topic);
        }
        for (int i = 0; i < 10; i++) {
            Message<?> received = kafkaGateway.receiveFromKafka();
            System.out.println(received);
        }
        System.out.println("Adding an adapter for a second topic and sending 10 messages...");
        addAnotherListenerForTopics(topic2);
        for (int i = 0; i < 10; i++) {
            String message = "bar" + i;
            System.out.println("Send to Kafka: " + message);
            kafkaGateway.sendToKafka(message, topic2);
        }
        for (int i = 0; i < 10; i++) {
            Message<?> received = kafkaGateway.receiveFromKafka();
            System.out.println(received);
        }
        context.close();
    }

    @Bean
    public IntegrationFlow toKafka(KafkaTemplate<?, ?> kafkaTemplate) {
        return flowDefinition -> flowDefinition
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate)
                        .messageKey("si.key"));
    }

    @Bean
    public IntegrationFlow fromKafkaFlow(ConsumerFactory<?, ?> consumerFactory) {
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(consumerFactory, topic))
                .channel((Channels c) -> c.queue("fromKafka"))
                .get();
    }

    /*
     * Boot's autoconfigured KafkaAdmin will provision the topics.
     */
    @Bean
    public NewTopic topic() {
        return new NewTopic(topic, 5, (short) 1);
    }

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(topic2, 5, (short) 1);
    }

    @Autowired
    private IntegrationFlowContext flowContext;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void addAnotherListenerForTopics(String... topics) {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
        // change the group id so we don't revoke the other partitions.
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG,
                consumerProperties.get(ConsumerConfig.GROUP_ID_CONFIG) + "x");
        flowContext.registration(IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(
                        new DefaultKafkaConsumerFactory<>(consumerProperties), topics))
                .channel("fromKafka")
                .get()).register();
    }

}
