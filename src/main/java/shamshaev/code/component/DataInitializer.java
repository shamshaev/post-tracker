package shamshaev.code.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.model.PostOffice;
import shamshaev.code.model.PostalItem;
import shamshaev.code.model.PostalItemType;
import shamshaev.code.model.TrackStatus;
import shamshaev.code.model.TrackStatusType;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.repository.PostalItemRepository;
import shamshaev.code.repository.TrackStatusRepository;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Consumer;

@Profile("!test")
@Slf4j
@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PostOfficeRepository postOfficeRepository;
    private final PostalItemRepository postalItemRepository;
    private final TrackStatusRepository trackStatusRepository;

    public void run(ApplicationArguments args) {
        if (postOfficeRepository.findAll().isEmpty()) {
            initializePostOffices();
        }
        if (postalItemRepository.findAll().isEmpty()) {
            initializePostalItems();
        }
        if (trackStatusRepository.findAll().isEmpty()) {
            initializeTrackStatuses();
        }
    }

    public void initializePostOffices() {
        Consumer<CSVRecord> savePostOffice = record -> {
            var postOffice = new PostOffice();

            postOffice.setPostCode(record.get("index"));
            postOffice.setName(record.get("opsname"));

            var region = record.get("region");
            var city = record.get("city");
            var address = city.isEmpty() ? region : region + ", " + city;
            postOffice.setAddress(address);

            postOfficeRepository.save(postOffice);
        };

        insertFromCSV("post_offices.csv", savePostOffice);
    }

    public void initializePostalItems() {
        Consumer<CSVRecord> savePostalItem = record -> {
            var postalItem = new PostalItem();

            postalItem.setPostalId(record.get("id"));
            postalItem.setType(PostalItemType.valueOf(record.get("type")));
            postalItem.setRecipientPostCode(record.get("index"));
            postalItem.setRecipientAddress(record.get("address"));
            postalItem.setRecipientName(record.get("name"));

            postalItemRepository.save(postalItem);
        };

        insertFromCSV("postal_items.csv", savePostalItem);
    }

    public void initializeTrackStatuses() {
        Consumer<CSVRecord> saveTrackStatus = record -> {
            var trackStatus = new TrackStatus();

            trackStatus.setType(TrackStatusType.valueOf(record.get("type")));
            var postalItem = postalItemRepository.findByPostalId(record.get("id"))
                    .orElseThrow(() -> new ResourceNotFoundException("PostalItem with postalId '" + record.get("id")
                            + "' not found"));
            trackStatus.setPostalItem(postalItem);
            var postalOffice = postOfficeRepository.findByPostCode(record.get("index"))
                    .orElseThrow(() -> new ResourceNotFoundException("PostOffice with postcode '" + record.get("index")
                            + "' not found"));
            trackStatus.setPostOffice(postalOffice);

            trackStatusRepository.save(trackStatus);
        };

        insertFromCSV("track_statuses.csv", saveTrackStatus);
    }

    private void insertFromCSV(String file, Consumer<CSVRecord> action) {
        try (Reader in = new InputStreamReader(new ClassPathResource("db.initial/" + file).getInputStream())) {
            CSVFormat.DEFAULT.builder()
                    .setHeader().setIgnoreHeaderCase(true)
                    .setAllowMissingColumnNames(true)
                    .setSkipHeaderRecord(true)
                    .build().parse(in).forEach(action);
        } catch (Exception e) {
            log.error("Unable to read from file {}", file, e);
        }
    }
}
