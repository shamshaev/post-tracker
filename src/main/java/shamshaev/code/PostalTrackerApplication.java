package shamshaev.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PostalTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostalTrackerApplication.class, args);
    }

}
