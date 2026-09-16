package com.yury.wikimedia.consumer;

import com.yury.wikimedia.consumer.configuration.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({ KafkaProperties.class })
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		WikimediaListener listener = context.getBean(WikimediaListener.class);
		listener.consume();
	}

}
