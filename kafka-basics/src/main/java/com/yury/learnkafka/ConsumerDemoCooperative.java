package com.yury.learnkafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConsumerDemoCooperative {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) {
        log.info("Starting consumer");

        String groupId = "bar-group";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);

        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("enable.auto.commit", "false");
        properties.setProperty("auto.commit.interval.ms", "5000");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        Thread main = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown detected. Let's exit the consumer");
            consumer.wakeup();

            try {
                main.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        consumer.subscribe(List.of("bar"));

        try {
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> {
                    log.info("key {}, value {}, partition {}, offset {}",
                            record.key(), record.value(), record.partition(), record.offset());
                    consumer.commitSync(Map.of(
                            new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1)));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    throw new RuntimeException("fuck");
                });
            }
        } catch (WakeupException e) {
            log.info("Consumer is shutting down");
        } finally {
            consumer.close();
            log.info("Consumer is closed");
        }
    }
}
