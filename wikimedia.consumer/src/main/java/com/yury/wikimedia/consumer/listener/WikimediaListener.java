package com.yury.wikimedia.consumer.listener;

import com.yury.wikimedia.consumer.service.WikimediaService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${opensearch.bulk-size}")
    private int bulkSize;

    @Scheduled(fixedRate = 5_000)
    public void consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(30));
        log.info("Fetched {} records from kafka", records.count());

        List<String> values = new java.util.ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            values.add(record.value());
        }

        for (int i = 0; i < values.size(); i += bulkSize) {
            List<String> batch = values.subList(i, Math.min(i + bulkSize, values.size()));
            try {
                wikimediaService.saveBulkWikies(batch);
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
