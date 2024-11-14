package shamshaev.code;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(info =
    @Info(
            title = "Post Tracker API",
            version = "1.0"
    )
)
@SpringBootApplication
@EnableJpaAuditing
public class PostalTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostalTrackerApplication.class, args);
    }

}
