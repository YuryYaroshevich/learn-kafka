package com.yury.wikimedia.consumer.listener;

import com.yury.wikimedia.consumer.service.WikimediaService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WikimediaListener {
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final WikimediaService wikimediaService;

    @Scheduled(fixedRate = 5_000)
    public void consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(30));
        log.info("Fetched {} records from kafka", records.count());

        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            try {
                wikimediaService.saveData(value);
            } catch (Exception e) {
                log.error("Failed to save data", e);
                for (TopicPartition partition : records.partitions()) {
                    long offset = records.records(partition).get(0).offset();
                    kafkaConsumer.seek(partition, offset);
                }
                throw e;
            }
        }
    }
}
