package net.peihuan.blogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlogapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogapiApplication.class, args);
	}

}
