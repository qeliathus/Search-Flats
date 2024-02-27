package by.potapchuk.flatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FlatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatServiceApplication.class, args);
	}

}
