package com.yury.wikimedia.consumer.listener;

import com.yury.wikimedia.consumer.service.WikimediaService;
import java.time.Duration;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
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
        ConsumerRecords<String, String> consumerRecords =
                kafkaConsumer.poll(Duration.ofSeconds(30));
        log.info("Fetched {} records from kafka", consumerRecords.count());

        List<ConsumerRecord<String, String>> records =
                StreamSupport.stream(consumerRecords.spliterator(), false).toList();

        try {
            for (int i = 0; i < records.size(); i += bulkSize) {
                List<ConsumerRecord<String, String>> batch =
                        records.subList(i, Math.min(i + bulkSize, records.size()));
                List<String> values = batch.stream().map(ConsumerRecord::value).toList();
                wikimediaService.saveBulkWikies(values);
            }
            kafkaConsumer.commitSync();
        } catch (Exception e) {
            log.error("Failed to save data", e);
            for (TopicPartition partition : consumerRecords.partitions()) {
                long offset = consumerRecords.records(partition).get(0).offset();
                kafkaConsumer.seek(partition, offset);
            }
            throw e;
        }
    }
}
