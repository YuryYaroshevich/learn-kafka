package com.yury.wikimedia.producer.configuration;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.HttpConnectStrategy;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import com.yury.wikimedia.producer.eventhandler.WikimediaChangeHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
public class EventSourceConfiguration {
    @Bean
    public BackgroundEventSource eventSource(@Value("${event-source.url}") String eventSourceUrl,
                                   WikimediaChangeHandler wikimediaChangeHandler) {
        HttpConnectStrategy connectStrategy = ConnectStrategy
                .http(URI.create(eventSourceUrl))
                .connectTimeout(5, TimeUnit.SECONDS);
        return new BackgroundEventSource.Builder(wikimediaChangeHandler, new EventSource.Builder(connectStrategy))
                .build();
    }
}
