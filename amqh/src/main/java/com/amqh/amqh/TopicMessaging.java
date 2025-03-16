package com.amqh.amqh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

// TopicMessaging: 토픽 메시지 발신, 수신
@Component
public class TopicMessaging {

    // logger
    private static final Logger log = LoggerFactory.getLogger(TopicMessaging.class);

    // jmsTopicTemplate
    private final JmsTemplate jmsTopicTemplate;

    // 생성자
    public TopicMessaging(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    // 메시지 발신 (토픽)
    public void sendTopicMessage(String destination, MessageDto dto) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(dto);
            jmsTopicTemplate.convertAndSend(destination, message);
            log.info("메시지 발신 성공: {}, {}", dto.getDestination(), message);
        } catch (Exception e) {
            throw new Exception("메시지 발신 실패 : " + e);
        }
    }

    // 메시지 수신 (토픽, 동기) => 안됨
    public MessageDto receiveTopicMessage(String destination) throws Exception {
        try {
            String message = (String) jmsTopicTemplate.receiveAndConvert(destination);
            ObjectMapper objectMapper = new ObjectMapper();
            MessageDto dto = objectMapper.readValue(message, MessageDto.class);
            log.info("메시지 수신 성공: {}, {}", dto.getDestination(), message);
            return dto;
        } catch (Exception e) {
            throw new Exception("메시지 수신 실패 : "  + e);
        }
    }

}
