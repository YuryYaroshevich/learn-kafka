package com.yury.wikimedia.producer;

import com.launchdarkly.eventsource.background.BackgroundEventSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(BackgroundEventSource backgroundEventSource) {
		return args -> {
			log.info("Starting receiving messages");
			backgroundEventSource.start();
			TimeUnit.SECONDS.sleep(30);
			log.info("Receiving messages was stopped");
		};
	}

}
