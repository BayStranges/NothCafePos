package com.nothcoffee.NothCoffeePOS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.nothcoffee.NothCoffeePOS.model")
@EnableJpaRepositories(basePackages = "com.nothcoffee.NothCoffeePOS.repository")
@ComponentScan(basePackages = "com.nothcoffee.NothCoffeePOS")
public class NothCoffeePosApplication {

	public static void main(String[] args) {
		SpringApplication.run(NothCoffeePosApplication.class, args);
	}

}
