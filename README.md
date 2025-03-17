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

- ActiveMQ 로컬 구축 및 Topic/Queue에서 Enqueue/Dequeue 구현 및 테스트
- ActiveMQ Enqueue, Dequeue 자바로 구현해 라이브러리(jar)로 만들기
- 동적 기능: 소비자는 한개의 토픽 및 큐만 구독 -> 여러개의 토픽 및 큐리스트로 구독
- 메시지(Oject) 형식: String mqName, String title, String context

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
  - ~/.m2/repository/com/library/amqh/1.0.0/amqh-1.0.0.jar
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
<summary><b> 🚀 ActiveMQ 학습</b></summary>
<br>

- ActiveMQ 개념 및 동적 컨슈머 구현: https://mpqm.tistory.com/218

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
