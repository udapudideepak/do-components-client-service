package com.test.components;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.test.components"})
//@EnableCirtcuitBreaker
@EntityScan(basePackages = {"com.test.components.entities"})
public class ComponentsApplication {

	@Bean(name="restTemplate")
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2);
	}


	public static void main(String[] args) {
		SpringApplication.run(ComponentsApplication.class, args);
	}
}
