package com.Matt.wiki;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class WikiApplication {
	private static final Log LOG = LogFactory.getLog(WikiApplication.class);

	public static void main(String[] args) {
//		SpringApplication.run(WikiApplication.class, args);
		SpringApplication app = new SpringApplication(WikiApplication.class);

		Environment env = app.run(args).getEnvironment();
		LOG.info("Start Successful");
		LOG.info("地址:\thttp://127.0.0.1:" + env.getProperty("server.port"));

	}

}
