package com.libary.amqh;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// HelperConfig: ActiveMQ 설정 파일 클래스
@Configuration
@EnableJms
@EnableConfigurationProperties(HelperProperties.class)
public class HelperConfig {

    // ActiveMQ 관련 설정 변수
    private final HelperProperties helperProperties;

    // 생성자
    public HelperConfig(HelperProperties helperProperties) {
        this.helperProperties = helperProperties;
    }

    // ActiveMQ connectionFactory 빈
    @Bean(name = "connectionFactory")
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(helperProperties.getBrokerUrl());
        factory.setUserName(helperProperties.getUsername());
        factory.setPassword(helperProperties.getPassword());
        return factory;
    }

    // JmsTopicTemplate 빈
    @Bean(name = "jmsTopicTemplate")
    public JmsTemplate jmsTopicTemplate(ActiveMQConnectionFactory factory) {
        JmsTemplate jmsTemplate = new JmsTemplate(factory);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setPubSubDomain(true); // Topic 으로 설정
        jmsTemplate.setExplicitQosEnabled(true); // 메시지 전송 품질 QOS 활성화
        jmsTemplate.setDeliveryPersistent(true); // 메시지의 영속성 비활성화
        jmsTemplate.setReceiveTimeout(3000); //  메시지의 유효 기간, -1이면 무한대기
        jmsTemplate.setTimeToLive(1800000); // 메시지를 수신하는 동안의 대기 시간
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT); // 메시지 전송 모드
        return jmsTemplate;
    }

    // jmsQueueTemplate 빈
    @Bean(name = "jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate(ActiveMQConnectionFactory factory) {
        JmsTemplate jmsTemplate = new JmsTemplate(factory);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setPubSubDomain(false); // Queue 으로 설정
        jmsTemplate.setExplicitQosEnabled(true); // 메시지 전송 품질 QOS 활성화
        jmsTemplate.setDeliveryPersistent(true); // 메시지의 영속성 비활성화
        jmsTemplate.setReceiveTimeout(3000); //  메시지의 유효 기간, -1이면 무한대기
        jmsTemplate.setTimeToLive(1800000); // 메시지를 수신하는 동안의 대기 시간
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT); // 메시지 전송 모드(영속화)
        return jmsTemplate;
    }

    // messageConverter 빈
    @Bean(name="jacksonJmsMessageConverter")
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_typeId");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("message", MessageDto.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }

    // dynamicListener 빈
    @Bean(name = "dynamicListener")
    public DynamicListener dynamicListener(ConnectionFactory connectionFactory) {
        List<String> queues = helperProperties.getQueues();
        List<String> topics = helperProperties.getTopics();
        Integer queueSessionMode = helperProperties.getQueueSessionMode();
        Integer topicSessionMode = helperProperties.getTopicSessionMode();
        String clientId = helperProperties.getClientId();
        Boolean retransmit = helperProperties.getRetransmit();
        return new DynamicListener(connectionFactory, queues, topics, queueSessionMode, topicSessionMode, clientId, retransmit);
    }

    // listenerManager 빈
    @Bean(name ="listenerManager")
    public ListenerManager listenerManager(DynamicListener dynamicListener){
        return new ListenerManager(dynamicListener);
    }

//    // Queue JmsListenerContainerFactory 빈
//    @Bean(name = "queueListenerContainerFactory")
//    public JmsListenerContainerFactory<?> queueListenerContainerFactory(ConnectionFactory connectionFactory) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(false); // Queue 활성화
//        return factory;
//    }
//
//    // Topic JmsListenerContainerFactory 빈
//    @Bean(name = "topicListenerContainerFactory")
//    public JmsListenerContainerFactory<?> topicListenerContainerFactory(ConnectionFactory connectionFactory) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(true); // Topic 활성화
//        return factory;
//    }

}
