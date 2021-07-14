package keyboard.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class SpringAuthJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAuthJwtApplication.class, args);
	}

	@Bean
	public SpringAppContext springAppContext() {
		return new SpringAppContext();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
}
