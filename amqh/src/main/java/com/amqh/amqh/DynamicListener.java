package com.amqh.amqh;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// DynamicListener: ActiveMQ를 이용해 동적으로 Queue 및 Topic 을 구독하는 클래스
@Component
public class DynamicListener {

    // logger
    private static final Logger log = LoggerFactory.getLogger(DynamicListener.class);

    // ActiveMQ ConnectionFactory
    private final ConnectionFactory connectionFactory;

    // 구독할 큐 이름 리스트
    private final List<String> queues;

    // 구독할 토픽 이름 리스트
    private final List<String> topics;

    // 생성된 ActiveMQ 커넥션 리스트
    private final List<Connection> connections = new ArrayList<>();

    // Session 모드(큐)
    private final Integer queueSessionMode;

    // Session 모드(토픽)
    private final Integer topicSessionMode;

    // 고유 Client ID
    private final String clientId;

    // 메시지 재전송 여부
    private final Boolean retransmit;

    // 생성자


    public DynamicListener(ConnectionFactory connectionFactory, List<String> queues, List<String> topics, Integer queueSessionMode, Integer topicSessionMode, String clientId, Boolean retransmit) {
        this.connectionFactory = connectionFactory;
        this.queues = queues;
        this.topics = topics;
        this.queueSessionMode = queueSessionMode;
        this.topicSessionMode = topicSessionMode;
        this.clientId = clientId;
        this.retransmit = retransmit;
    }

    private String formatJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObj = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        } catch (Exception e) {
            log.error("JSON 포맷팅 오류: {}", e.getMessage());
            return json;
        }
    }

    // 세션 모드에 따른 플래그 반환
    private Boolean checkSessionMode(Integer sessionMode) {
        if (sessionMode == 0) {
            return true;
        } else if (sessionMode == 1 || sessionMode == 2 || sessionMode == 3 || sessionMode == 4) {
            return false;
        } else {
            log.error("Session Mode 오류: {}", sessionMode);
            return false;
        }
    }

    // 특정 Queue 에 대한 메시지 리스너 시작 메소드
    private void startQueueListener(String queueName) {
        try {
            // ActiveMQ 와의 연결 생성, 시작, 연결 목록에 추가
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connections.add(connection);

            // 세션, 큐, 메시지 소비사 생성
            Session session = connection.createSession(checkSessionMode(queueSessionMode), queueSessionMode);
            Queue queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue);

            // 메시지 리스너 설정 if: 일반, else: 재전송
            if(!retransmit){
                consumer.setMessageListener(message -> {
                    try {
                        if (message instanceof TextMessage textMessage) {
                            String rawText = textMessage.getText();
                            log.info("메시지 수신 성공: {}, {}", queueName, rawText);
                        } else {
                            log.warn("메시지 타입이 TextMessage 가 아님: {}", message.getClass().getName());
                        }
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생: {}", e.getMessage());
                    }
                });
            } else {
                consumer.setMessageListener(message -> {
                    try {
                        if (message instanceof TextMessage textMessage) {
                            String rawText = textMessage.getText();
                            log.info("메시지 수신 성공: {}, {}", queueName, rawText);
                            // 정상적으로 처리되었을 경우 트랜잭션 커밋
                            session.commit();
                            log.info("트랜잭션 커밋 성공: {}", queueName);
                        } else {
                            log.warn("메시지 타입이 TextMessage 가 아님: {}", message.getClass().getName());
                        }
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생: {}", e.getMessage());
                        try {
                            // 예외 발생 시 메시지를 다시 받도록 롤백
                            session.rollback();
                        } catch (JMSException jmsException) {
                            log.error("트랜잭션 롤백 실패: {}", jmsException.getMessage());
                        }
                    }
                });
            }
            log.info("메시지 큐 구독 성공 : {}", queueName);
        } catch (Exception e) {
            log.error("메시지 큐 구독 실패 : {}, {}", queueName, e.getMessage());
        }
    }

    // 특정 Topic 에 대한 메시지 리스너 시작 메소드
    private void startTopicListener(String topicName) {
        try {

            // ActiveMQ 와의 연결 생성, 시작, 연결 목록에 추가
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(clientId + topicName); // Durable Subscription을 위한 클라이언트 ID 설정
            connection.start();
            connections.add(connection);

            // 세션 및 토픽 생성
            Session session = connection.createSession(checkSessionMode(topicSessionMode), topicSessionMode);
            Topic topic = session.createTopic(topicName);

            // 토픽에 대한 메시지 소비자 생성 if: 일반, else: 재전송
            if(!retransmit) {
                MessageConsumer consumer = session.createConsumer(topic);
                    consumer.setMessageListener(message -> {
                    try {
                        if (message instanceof TextMessage textMessage) {

                            // 메시지 본문 가져와서 JSON 포맷 적용
                            String rawText = textMessage.getText();
                            log.info("메시지 수신 성공: {}, {}", topicName, rawText);
                        } else {
                            log.warn("메시지 타입이 TextMessage 가 아님: {}", message.getClass().getName());
                        }
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생: {}", e.getMessage());
                    }
                });
            } else {
                MessageConsumer consumer = session.createDurableSubscriber(topic, "subscription-" + clientId + topicName);
                consumer.setMessageListener(message -> {
                    try {
                        if (message instanceof TextMessage textMessage) {
                            String rawText = textMessage.getText();
                            log.info("메시지 수신 성공: {}, {}", topicName, rawText);

                            // 메시지가 정상적으로 처리되었을 경우 명시적으로 ACK
                            log.info("메시지 ACK 전송: {}", topicName);
                            message.acknowledge();
                        } else {
                            log.warn("메시지 타입이 TextMessage 가 아님: {}", message.getClass().getName());
                        }
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생: {}", e.getMessage());
                    }
                });

                log.info("메시지 토픽 구독 성공 : {}", topicName);
            }

        } catch (Exception e) {
            log.error("메시지 토픽 구독 실패 : {}, {}", topicName, e.getMessage());
        }
    }

    // Listener 의 @PostConstruct 를 이용해 어플리케이션 시작시 실행
    public void startListening() {
        // 큐 구독 설정
        for (String queueName : queues) {
            startQueueListener(queueName);
        }

        // 토픽 구독 설정
        for (String topicName : topics) {
            startTopicListener(topicName);
        }
    }

    // Listener 의 @PreDestroy 를 이용해 애플리케이션 종료시 실행
    public void stopListening() {
        // ActiveMQ의 커넥션을 모두 종료
        for (Connection connection : connections) {
            try {
                connection.close();
                log.info("구독 종료");
            } catch (Exception e) {
                log.error("구독 종료 중 오류 발생", e);
            }
        }
        connections.clear();
    }
}
