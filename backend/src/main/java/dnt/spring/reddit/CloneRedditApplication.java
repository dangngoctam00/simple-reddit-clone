package dnt.spring.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import dnt.spring.reddit.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class CloneRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloneRedditApplication.class, args);
	}

}
