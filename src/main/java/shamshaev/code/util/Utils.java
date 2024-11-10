package shamshaev.code.util;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Utils {
    @Bean
    public Faker getFaker() {
        return new Faker(Locale.of("ru"));
    }
}
