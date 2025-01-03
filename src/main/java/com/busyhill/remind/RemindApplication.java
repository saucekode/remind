package com.busyhill.remind;

import com.busyhill.remind.config.OauthAccessConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(OauthAccessConfig.class)
public class  RemindApplication implements CommandLineRunner {

	private final OauthAccessConfig oauthAccessConfig;

    public RemindApplication(OauthAccessConfig oauthAccessConfig) {
        this.oauthAccessConfig = oauthAccessConfig;
    }

    public static void main(String[] args) {
		SpringApplication.run(RemindApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Logger logger = LoggerFactory.getLogger(RemindApplication.class);

		logger.info("----------------------------------------");
		logger.info("Configuration properties");
		logger.info("   oauth.id is {}", oauthAccessConfig.getId());
		logger.info("   oauth.secret is {}", oauthAccessConfig.getSecret());
		logger.info("----------------------------------------");
	}
}
