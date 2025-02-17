package br.com.orderserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.orderserver.client")
public class OrderserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderserverApplication.class, args);
	}

}
