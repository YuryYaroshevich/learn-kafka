package com.yury.wikimedia.consumer.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import com.yury.wikimedia.consumer.service.WikimediaService;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class WikimediaListener {
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final WikimediaService wikimediaService;

    public void consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
        for (ConsumerRecord<String, String> record : records) {
            wikimediaService.saveData(record.value());
        }
    }
}