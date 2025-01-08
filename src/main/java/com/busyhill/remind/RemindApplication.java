package com.busyhill.remind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
public class  RemindApplication {

    public static void main(String[] args) {
		SpringApplication.run(RemindApplication.class, args);
	}
}
