package com.fornet.test1;

import com.fornet.amqh.MessageDto;
import com.fornet.amqh.QueueMessaging;
import com.fornet.amqh.TopicMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final TopicMessaging topicMessaging;
    private final QueueMessaging queueMessaging;

    // 메시지 발신(큐)
    @PostMapping("/send-queue")
    public ResponseEntity<MessageDto> sendQueueMessage(@RequestBody MessageDto dto) throws Exception {

        queueMessaging.sendQueueMessage(dto.getDestination(), dto);

        return ResponseEntity.ok(dto);
    }

    // 메시지 발신(토픽)
    @PostMapping("/send-topic")
    public ResponseEntity<MessageDto> sendTopicMessage(@RequestBody MessageDto dto) throws Exception {

        topicMessaging.sendTopicMessage(dto.getDestination(), dto);

        return ResponseEntity.ok(dto);
    }

    // 메시지 동기 수신 (큐)
    @GetMapping("/receive-queue")
    ResponseEntity<MessageDto> receiveQueueMessage(@RequestParam("destination") String destination) throws Exception {

        MessageDto dto = queueMessaging.receiveQueueMessage(destination);

        return ResponseEntity.ok(dto);
    }

    // 메시지 동기 수신 (토픽) => 안됨
    @GetMapping("/receive-topic")
    ResponseEntity<MessageDto> receiveTopicMessage(@RequestParam("destination") String destination) throws Exception {

        MessageDto dto = topicMessaging.receiveTopicMessage(destination);

        return ResponseEntity.ok(dto);
    }

}

