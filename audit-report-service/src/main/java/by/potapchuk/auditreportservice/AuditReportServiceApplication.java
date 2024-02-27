package by.potapchuk.auditreportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuditReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditReportServiceApplication.class, args);
	}

}
