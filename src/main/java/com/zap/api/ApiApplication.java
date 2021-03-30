package com.zap.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ApiApplication /* implements CommandLineRunner */ {

//	@Autowired
//	private DataMock dataMock;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		dataMock.loadDBMocked();
//	}

}
