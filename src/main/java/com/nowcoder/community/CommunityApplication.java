package com.nowcoder.community;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommunityApplication {

	@PostConstruct
	public void init(){
		//解决netty底层冲突的问题
		//netty4utils
		System.setProperty("es.set.netty.runtime.available.processes","false");
	}
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
		/*UserService userService = new UserServiceImpl();
		userService.zc();*/
	}

}
