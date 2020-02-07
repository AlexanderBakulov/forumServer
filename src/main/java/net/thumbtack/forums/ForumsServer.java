package net.thumbtack.forums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class ForumsServer {

	public static void main(String[] args) {
		SpringApplication.run(ForumsServer.class, args);
	}

}
