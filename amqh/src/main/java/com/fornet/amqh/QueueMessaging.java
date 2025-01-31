package com.fornet.amqh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

// QueueMessaging: 큐 메시지 발신, 수신
@Component
public class QueueMessaging {

    // logger
    private static final Logger log = LoggerFactory.getLogger(QueueMessaging.class);

    // jmsQueueTemplate
    private final JmsTemplate jmsQueueTemplate;

    // 생성자
    public QueueMessaging(@Qualifier("jmsQueueTemplate") JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    // 메시지 발신 (큐)
    public void sendQueueMessage(String destination, MessageDto dto) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(dto);
            jmsQueueTemplate.convertAndSend(destination, message);
            log.info("메시지 발신 성공: {}, {}", dto.getDestination(), message);
        } catch (Exception e) {
            throw new Exception("메시지 발신 실패 : " + e);
        }
    }

    // 메시지 수신 (큐, 동기)
    // 1. point to point 연결인 큐에서 클라이언트가 종료된 상황에서 사용
    // 2. 특정 큐에 대한 구독이 없는 상황에서 생산자가 큐에 메시지를 쌓았을때 구독하지 않은 클라이언트가 사용
    public MessageDto receiveQueueMessage(String destination) throws Exception {
        try {
            String message = (String) jmsQueueTemplate.receiveAndConvert(destination);
            if (message == null) {
                log.warn("큐 '{}' 에서 메시지를 찾을 수 없습니다.", destination);
                return null;  // 메시지가 없으면 null 반환
            }
            ObjectMapper objectMapper = new ObjectMapper();
            MessageDto dto = objectMapper.readValue(message, MessageDto.class);
            log.info("메시지 수신 성공: {}, {}", dto.getDestination(), message);
            return dto;
        } catch (Exception e) {
            throw new Exception("메시지 수신 실패 : " + e);
        }
    }

}
