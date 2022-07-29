package com.cinemapp.demo;

import com.cinemapp.demo.learning.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public Customer customer(@Qualifier("address2") String address) {
		var customer = new Customer("Clara Foster", address);
		System.out.println(customer);
		return customer;
	}

//	@Bean
//	public Customer temporary(@Autowired Customer customer) {
//		System.out.println(customer);
//		return customer;
//	}
}
