package com.jramcon398.str_consumer.listeners;

import com.jramcon398.str_consumer.model.KafkaMessage;
import com.jramcon398.str_consumer.repository.KafkaMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StrConsumerListener {

    @Autowired
    private KafkaMessageRepository kafkaMessageRepository;

    @KafkaListener(groupId = "group-1",
            topicPartitions = @TopicPartition(topic = "str-topic", partitions = {"0"}),
            containerFactory = "validMessageContainerFactory")
    public void listener1(String message) {
        log.info("LISTENER1 ::: Recibiendo un mensaje {}", message);
        saveMessage(message);
    }

    @KafkaListener(groupId = "group-1",
            topicPartitions = @TopicPartition(topic = "str-topic", partitions = {"1"}),
            containerFactory = "validMessageContainerFactory")
    public void listener2(String message) {
        log.info("LISTENER2 ::: Recibiendo un mensaje {}", message);
        saveMessage(message);
    }

    @KafkaListener(groupId = "group-2", topics = "str-topic",
            containerFactory = "validMessageContainerFactory")
    public void listener3(String message) {
        log.info("LISTENER3 ::: Recibiendo un mensaje {}", message);
        saveMessage(message);
    }

    private void saveMessage(String content) {
        KafkaMessage msg = new KafkaMessage(content, LocalDateTime.now());
        kafkaMessageRepository.save(msg);
        log.info("Mensaje persistido en BD: {}", content);
    }
}