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
import shamshaev.code.model.PostalItem;
import shamshaev.code.model.PostalItemType;

import java.util.Random;

@Getter
@Component
public class ModelGenerator {
    private Model<PostOffice> postOfficeModel;
    private Model<PostalItem> postalItemModel;

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

        postalItemModel = Instancio.of(PostalItem.class)
                .ignore(Select.field(PostalItem::getId))
                .supply(Select.field(PostalItem::getPostalId), () -> faker.regexify("[0-9]{14}"))
                .supply(Select.field(PostalItem::getType), ModelGenerator::randomPostalItemType)
                .supply(Select.field(PostalItem::getRecipientPostCode), () -> faker.address().postcode())
                .supply(Select.field(PostalItem::getRecipientAddress), () -> faker.address().city())
                .supply(Select.field(PostalItem::getRecipientName), () -> faker.name().name())
                .toModel();
    }

    private static PostalItemType randomPostalItemType() {
        PostalItemType[] types = PostalItemType.values();
        return types[new Random().nextInt(types.length)];
    }
}
