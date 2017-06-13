package com.franciscocalaca.led;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Led.class})
public class Led {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Led.class, args);
	}

}