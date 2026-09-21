package com.yury.wikimedia.producer.eventhandler;

import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WikimediaChangeHandler implements BackgroundEventHandler {
    private final KafkaProducer<String, String> kafkaProducer;

    private final String topic;

    public WikimediaChangeHandler(
            KafkaProducer<String, String> kafkaProducer, @Value("${kafka.topic}") String topic) {
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;
    }

    @Override
    public void onOpen() throws Exception {}

    @Override
    public void onClosed() throws Exception {}

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        log.debug("Sending to kafka: {}", messageEvent.getData());

        kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) throws Exception {}

    @Override
    public void onError(Throwable t) {
        log.error("Error in stream reading", t);
    }
}
