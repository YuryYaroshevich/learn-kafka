package com.yury.wikimedia.consumer.configuration;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private final String bootstrapServer;

    private final String groupId;

    private final String topicName;
}
