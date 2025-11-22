package github.com.LimaDevCod3r;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PontoCertoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PontoCertoApplication.class, args);
	}

}
