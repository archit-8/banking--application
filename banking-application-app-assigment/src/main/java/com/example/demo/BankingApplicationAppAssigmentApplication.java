package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class BankingApplicationAppAssigmentApplication {

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
				long accNo = ThreadLocalRandom.current()
						.nextLong(10_000_000_000L, 100_000_000_000L);
				System.out.println(accNo);

		}

		SpringApplication.run(BankingApplicationAppAssigmentApplication.class, args);
	}

}
