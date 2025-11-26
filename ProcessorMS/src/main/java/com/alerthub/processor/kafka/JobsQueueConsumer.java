package com.alerthub.processor.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobsQueueConsumer {

    public JobsQueueConsumer() {
        log.info("JobsQueueConsumer bean created");
    }

    @KafkaListener(
            topics = "${kafka.topics.jobs}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        log.info("A message from Kafka (actions-jobs): {}", message);
    }
}
