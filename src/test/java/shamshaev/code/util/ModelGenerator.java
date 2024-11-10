package shamshaev.code.util;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import shamshaev.code.model.PostOffice;

@Getter
@Component
public class ModelGenerator {
    private Model<PostOffice> postOfficeModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    public void init() {
        postOfficeModel = Instancio.of(PostOffice.class)
                .ignore(Select.field(PostOffice::getId))
                .supply(Select.field(PostOffice::getPostCode), () -> faker.address().postcode())
                .supply(Select.field(PostOffice::getName), () -> faker.company().name())
                .supply(Select.field(PostOffice::getAddress), () -> faker.address().city())
                .toModel();
    }
}
