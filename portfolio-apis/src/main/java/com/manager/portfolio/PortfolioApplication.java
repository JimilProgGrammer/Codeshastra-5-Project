package com.manager.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.manager.portfolio.utils",
								"com.manager.portfolio.constants",
								"com.manager.portfolio.controllers",
								"com.manager.portfolio.services",
								"com.manager.portfolio.dto",
								"com.manager.portfolio.repositories"})
public class PortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

}
