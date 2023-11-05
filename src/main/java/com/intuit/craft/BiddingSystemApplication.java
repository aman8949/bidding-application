package com.intuit.craft;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Bidding Application"))
public class BiddingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingSystemApplication.class, args);
	}

}
