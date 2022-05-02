package com.fbxmtjqj.tdd.membership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TddMembershipApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddMembershipApplication.class, args);
	}

}
