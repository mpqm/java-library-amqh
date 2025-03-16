# ActiveMQHelper
<div align="center">
    <img  style="width: 50%" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkikDKbRDhuUVOtQxg5m-A5et65Hla8WD8og&s">
</div>
<!-- <div align=center>
    <h3>
        🌐 시연영상
        <a href="{실행동영상 유튜브 링크}">유튜브링크</a>
    </h3>
</div> -->

<br>

## 👨🏻‍🏫 프로젝트 소개
<details>
<summary><b> 📌 프로젝트 개요</b></summary>
<br>

- 프로젝트 구조
  - activemq - ActiveMQ Broker
  - amqh - ActiveMQHelper Library
  - test - spring boot server 1, 2, 3
- 요구사항
  - ActiveMQ 로컬 구축 및 Topic/Queue에서 Enqueue/Dequeue 구현 및 테스트</li>
  - ActiveMQ Enqueue, Dequeue 자바로 구현해 라이브러리(jar)로 만들기</li>
  - 동적 기능: 소비자는 한개의 토픽 및 큐만 구독 -> 여러개의 토픽 및 큐리스트로 구독</li>
  - 메시지(Oject) 형식: String mqName, String title, String context</li>

</details>

<br>

<details>
<summary><b> 🏃 프로젝트 실행</b></summary>
<br>

```bash
- activemqtest1, 2, 3
  - 각 모듈 실행시 Run > Edit Configurations
  - Run Configuration > working directory
  - 서브모듈 경로로로 설정
- activemqhelpber
  - clean & install
  - ~/.m2/repository/com/fornet/activemqhelpber/1.0.0/activemqhelpber-1.0.0.jar
```

```properties
activemq.broker-url=tcp://localhost:61616 #ActiveMQ 서버 URL
activemq.username=admin                   #ActiveMQ admin 계정 이름
activemq.password=admin                   #ActiveMQ admin 계정 비밀번호
activemq.explicit-qos-enabled=true        #ActiveMQ 전송 품질 QOS 활성화
activemq.delivery-persistent=true         #ActiveMQ 큐 영속성 활성화(메시지 유실 방지), false면 메시지 유실 허용
activemq.receive-timeout=3000             #ActiveMQ 메시지 수신 시간 (3초)
activemq.time-to-live=180000              #ActiveMQ 메시지 유효 시간 (3분)
activemq.topics=topic1,topic2,topic3      #ActiveMQ topic 이름 리스트
activemq.queues=queue1,queue2,queue3      #ActiveMQ queue 이름 리스트
activemq.queueSessionMode=0               #ActiveMQ Queue Session Mode 
activemq.topicSessionMode=0               #ActiveMQ Topic Session Mode
activemq.clientId=client1                 #ActiveMQ 클라이언트 ID
activemq.retransmit=false                 #ActiveMQ 메시지 재전송 여부
```

</details>

<br>

<details>
<summary><b> 🚀 ActiveMQ </b></summary>
<br>

- ActiveMQ 개념 
  - queue
    - 동기적 처리, 비 실시간성
    - 메시지를 보내는 쪽과 받는 쪽이 1:1로 연결되어 있는 방식 
  - topic
    - 비동기적 처리, 실시간성
    - 메시지를 보내는 쪽과 받는 쪽이 1:N으로 연결되어 있는 방식
    - 지원하는 receive() 메서드는 동기적인 방식으로 메시지를 받으므로 사용못함
  - virtualTopic
    - virtualTopic은 여러개의 큐에 메시지를 전달하는 방식
    - 여러개의 큐에 메시지를 전달할 때 사용 / 생산자는 topic처럼 소비자는 큐처럼 사용
    - 실제 생산자는 토픽 template으로 메시지를 발신
    - 소비자는 큐 template으로 토픽 destination에 있는 메시지를 수신
    - 만약 virtualTopic을 queue로 구독중인 경우 topic 처럼 동작

- JMS 메시지 헤더( 헤더 / 필드 )
  - Message ID / 메시지 고유 식별자 / 추적 및 중복 메시지
  - Destination / 목적지 큐 또는 토픽
  - Correlation ID / 메시지간 동일한 correlationId를 설정해 응답과 요청을 연결.
  - Group: 메시지 그룹 / 동일 메시지 그룹 ID 면 같은 소비자가 처리 / 순서 보장 메시지
  - Sequence: 메시지 그룹 내 메시지 순서 / 마지막 메시지의 경우 -1을 설정해 그룹을 종료
  - Expiration: 메시지 만료 시간 / 만료된 메시지는 Dead Letter Queue(DLQ)로 이동
  - Persistence: 메시지 영속성 여부 / 메시지가 브로커에 저장되어 브로커 재시작 시에도 유지
  - Priority: 메시지 우선순위 / 0~9 사이의 값을 가지며, 높은 숫자가 더 높은 우선순위를 의미
  - Redelivered	메시지 재전송 여부 / 메시지가 이전에 소비자에게 전달되었는지 여부 / 재처리 로직에 사용
  - Reply To: 응답 메시지 목적지 / 요청 응답 패턴에서 응답 메시지가 전송될 큐나 토픽을 지정.
  - Timestamp: 메시지 생성 시간
  - Type: 메시지 논리적 유형
  - _typeId: MappingJackson2MessageConverter에서 _typeId를 사용해 JSON 메시지를 특정 클래스로 변환

- Message ID 구조
  - ID:`<hostname>-<connectionID>-<sessionID>:<producerID>:<sequenceID>:<batchID>:<attemptID>`
  - ID Prefix: 메시지 ID의 시작을 나타내는 고정된 접두사 
  - Hostname:  메시지를 생성한 클라이언트가 실행 중인 호스트 이름 또는 브로커 ID / 클러스터 환경에서 메시지 추적 
  - Connection ID: 브로커와 클라이언트 간의 특정 연결을 식별하는 ID / 각 연결은 고유한 ID를 가짐 
  - Session ID: JMS 세션을 식별하는 고유한 ID / 한 클라이언트 연결에서 여러 세션이 생성가능 
  - Producer ID (_): 메시지를 생성한 특정 메시지 프로듀서를 식별하는 ID
  - Sequence ID (_): 해당 프로듀서가 생성한 메시지의 순서 번호 
  - Batch ID (_): 메시지가 배치(batch) 처리 중 생성되었는지 여부를 나타냄 / 처리X 기본값 1 
  - Attempt ID (_): 재전송 횟수나 메시지를 소비자에게 전달하려는 시도를 나타냄 / 첫 시도라면 1이고

- JmsTemplate 설정
  - jmsTemplate.setPubSubDomain(false); // Queue 으로 설정
  - jmsTemplate.setPubSubDomain(true); // Topic 으로 설정
  - jmsTemplate.setExplicitQosEnabled(true); // 메시지 전송 품질 QOS 활성화
  - jmsTemplate.setDeliveryPersistent(true); // 메시지의 영속성 비활성화
  - jmsTemplate.setReceiveTimeout(3000); //  메시지의 유효 기간, -1이면 무한대기
  - jmsTemplate.setTimeToLive(1800000); // 메시지를 수신하는 동안의 대기 시간
  - jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT); // 메시지 전송 모드(영속화)

- Jms Session Mode
  - SESSION_TRANSACTED: 0 / 트랜잭션 기반으로 메시지를 처리 명시적으로 commit()을 호출해야 메시지가 정상적으로 처리됨. rollback() 시 메시지가 재 전송
  - AUTO_ACKNOWLEDGE: 1 / 기본값, 메시지를 읽으면 자동으로 ACK 전송, 예외 발생 시 메시지를 재처리 불가
  - CLIENT_ACKNOWLEDGE: 2 / 수동 ACK 모드, 명시적으로 message.acknowledge()를 호출해야 ActiveMQ에서 메시지 삭제, 예외 발생 시 ACK를 안보내면 메시지가 재전송
  - DUPS_OK_ACKNOWLEDGE: 3 / AUTO_ACKNOWLEDGE보다 성능 최적화. 메시지를 중복 허용하는 대신 일부 메시지가 중복 가능
  - INDIVIDUAL_ACKNOWLEDGE: 4 / ActiveMQ 전용 확장 모드로 개별 메시지 단위로 ACK 가능. message.acknowledge()를 호출하면 해당 메시지만 ACK

- 메시지 재처리 방법
  - producer & broker
    - Producer에서 DeliveryMode.PERSISTENT 설정하면 브로커가 메시지를 저장함
    - ActiveMQ 설정에서 persistenceAdapter 활성화 필요
  - queue
    - Session 트랜잭션(SESSION_TRANSACTED) 사용 
    - Queue 메시지를 받을 때 트랜잭션을 적용하여 정상적으로 처리되었을 때만 commit() 수행
    - 예외 발생 시 rollback()을 수행하여 메시지가 다시 ActiveMQ로 돌아감 메시지 소실 방지
  - topic
    - 토픽은 기본적으로 비영속적 createDurableSubscriber()를 사용해 Durable Subscription 적용
    - setClientID() 설정으로 컨슈머가 오프라인이어도 메시지 저장 가능
    - Session.CLIENT_ACKNOWLEDGE 사용: 메시지를 받은 후 명시적으로 acknowledge() 호출 → 예외 발생 시 메시지 재전송
  - Redelivery Policy(메시지 재전송 정책 설정)
    - SESSION_TRANSACTED 및 CLIENT_ACKNOWLEDGE를 사용하면 Redelivery Policy와 함께 활용 가능
    - ActiveMQ 설정에서 Redelivery Policy 적용 가능 (maximumRedeliveries, backOffMultiplier 설정 등)

</details>

<br>

<details>
<summary><b> 🎮 기술 스택</b></summary>
<br>

| **CATEGORY**         | **SKILLS**                                                                                                         | 
|----------------------|--------------------------------------------------------------------------------------------------------------------|
| **LANGUAGE** | ![JAVA](https://img.shields.io/badge/java-6DB33F?style=for-the-badge&logo=spring&logoColor=white)                  
| **TEST**             | ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |

</details>
