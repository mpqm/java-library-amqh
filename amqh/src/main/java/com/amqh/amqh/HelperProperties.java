package com.amqh.amqh;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

// HelperProperties: ActiveMQ 설정 프로퍼티 클래스
@Component
@ConfigurationProperties(HelperProperties.PREFIX)
public class HelperProperties {

    // 설정 변수 PREFIX
    public static final String PREFIX = "activemq";

    // ActiveMQ 브로커 서버 Url
    private String brokerUrl;

    // ActiveMQ admin 계정 이름
    private String username;

    // ActiveMQ admin 계정 비밀번호
    private String password;

    // 큐 목록, Relaxed Binding을 이용해 String => List<String>
    private List<String> queues;

    // 토픽 목록
    private List<String> topics;

    // Session 모드(큐)
    private int queueSessionMode;

    // Session 모드(토픽)
    private int topicSessionMode;

    // 클라이언트 ID(고유값)
    private String clientId;

    // 메시지 재전송 여부
    private Boolean retransmit;

    public Boolean getRetransmit() {
        return retransmit;
    }

    public void setRetransmit(Boolean retransmit) {
        this.retransmit = retransmit;
    }

    public int getQueueSessionMode() {
        return queueSessionMode;
    }

    public void setQueueSessionMode(int queueSessionMode) {
        this.queueSessionMode = queueSessionMode;
    }

    public int getTopicSessionMode() {
        return topicSessionMode;
    }

    public void setTopicSessionMode(int topicSessionMode) {
        this.topicSessionMode = topicSessionMode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    // getter & setter
    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getQueues() {
        return queues;
    }

    public void setQueues(List<String> queues) {
        this.queues = queues;
    }
}