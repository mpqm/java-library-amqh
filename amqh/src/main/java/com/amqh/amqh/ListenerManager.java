package com.amqh.amqh;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

// ListenerManager: 애플리케이션의 라이프사이클 동안 ActiveMQ 리스너의 시작 및 종료를 관리하는 클래스.
@Component
public class ListenerManager {

    // DynamicListener
    private final DynamicListener dynamicListener;

    // 생성자
    public ListenerManager(DynamicListener dynamicListener) {
        this.dynamicListener = dynamicListener;
    }

    // 애플리케이션 시작 시 메시지 리스너 시작
    @PostConstruct
    public void startListeners() {
        dynamicListener.startListening();
    }

    // 애플리케이션 종료 시 메시지 리스너 종료
    @PreDestroy
    public void stopListeners() {
        dynamicListener.stopListening();
    }
}
