package com.yury.wikimedia.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class WikimediaListener {
    private final KafkaConsumer<String, String> kafkaConsumer;


    public void consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
        log.info("Received {} records", records.count());
    }
}
