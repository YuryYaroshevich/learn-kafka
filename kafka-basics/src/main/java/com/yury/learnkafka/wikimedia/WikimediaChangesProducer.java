package com.yury.learnkafka.wikimedia;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

// https://stream.wikimedia.org/v2/stream/recentchange
public class WikimediaChangesProducer {

    private static final String WIKIMEDIA_TOPIC = "wikimedia.recentchange";

    private static final String EVENT_SOURCE_URL = "https://stream.wikimedia.org/v2/stream/recentchange";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
    }
}
