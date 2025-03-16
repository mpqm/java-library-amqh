package com.amqh.test2;

import com.amqh.amqh.HelperConfig;
import com.amqh.amqh.QueueMessaging;
import com.amqh.amqh.TopicMessaging;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@Import(HelperConfig.class)
public class AmqhConfig {

    @Bean
    public QueueMessaging queueMessaging(@Qualifier("jmsQueueTemplate") JmsTemplate jmsTemplate){
        return new QueueMessaging(jmsTemplate);
    }

    @Bean
    public TopicMessaging topicMessaging(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTemplate){
        return new TopicMessaging(jmsTemplate);
    }

}
