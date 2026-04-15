package com.jramcon398.str_consumer.repository;

import com.jramcon398.str_consumer.model.KafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
}