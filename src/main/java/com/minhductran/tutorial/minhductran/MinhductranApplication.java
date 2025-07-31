package com.minhductran.tutorial.minhductran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MinhductranApplication {
	public static void main(String[] args) {
		SpringApplication.run(MinhductranApplication.class, args);
	}
}
