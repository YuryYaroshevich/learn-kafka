package com.yury.learnkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerDemoWithCallback {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerDemoWithCallback.class);

    public static void main(String[] args) {
        log.info("Starting producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("batch.size", "400");
        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int j = 0; j < 10; j++) {
            for (int i = 1; i <= 30; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>("foo", "hello from producer " + i);
                kafkaProducer.send(record, ((metadata, exception) -> {
                    if (exception == null) {
                        log.info("Received metadata. Topic {}, parition {}, offset {}, timestamp {}.",
                                metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
                    } else {
                        log.error("Failed to send message.", exception);
                    }
                }));
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        kafkaProducer.flush();
        kafkaProducer.close();

        log.info("Message has been sent");
    }
}