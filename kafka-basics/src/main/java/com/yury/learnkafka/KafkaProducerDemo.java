package com.yury.learnkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerDemo.class);

    public static void main(String[] args) {
        log.info("Starting producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>("foo", "hello from java!");

        kafkaProducer.send(record);

        kafkaProducer.flush();
        kafkaProducer.close();

        log.info("Message has been sent");
    }
}