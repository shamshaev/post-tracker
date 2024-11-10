package shamshaev.code.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import shamshaev.code.model.PostOffice;
import shamshaev.code.repository.PostOfficeRepository;
import java.io.FileReader;
import java.io.Reader;

@Slf4j
@Component
@AllArgsConstructor
public class PostOfficeInitializer implements ApplicationRunner {
    private final PostOfficeRepository postOfficeRepository;

    public void run(ApplicationArguments args) {
        if (postOfficeRepository.findAll().isEmpty()) {
            insertPostOfficesFromCSV();
        }
    }

    void insertPostOfficesFromCSV() {
        try (Reader in = new FileReader(new ClassPathResource("db.initial/post_offices.csv").getFile())) {
            CSVFormat.EXCEL.builder()
                    .setDelimiter(';').setHeader("code", "name", "region", "city")
                    .setAllowMissingColumnNames(true).setSkipHeaderRecord(true)
                    .build().parse(in).forEach(record -> {
                        var postOffice = new PostOffice();

                        postOffice.setPostCode(record.get("code"));
                        postOffice.setName(record.get("name"));

                        var region = record.get("region");
                        var city = record.get("city");
                        var address = city.isEmpty() ? region : region + ", " + city;
                        postOffice.setAddress(address);

                        postOfficeRepository.save(postOffice);
                    });
        } catch (Exception e) {
            log.error("Unable to read CSV file for PostOffices initialization", e);
        }
    }
}
