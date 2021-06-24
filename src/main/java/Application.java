import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.move_up")
@EnableJpaRepositories("com.move_up.repository")
@EntityScan("com.move_up.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Application {
	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}
}
