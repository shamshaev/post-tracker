package shamshaev.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import shamshaev.code.dto.TrackStatusUpdateDTO;
import shamshaev.code.mapper.TrackStatusMapper;
import shamshaev.code.model.TrackStatus;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.repository.PostalItemRepository;
import shamshaev.code.repository.TrackStatusRepository;
import shamshaev.code.util.ModelGenerator;
import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TrackStatusControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrackStatusRepository statusRepository;

    @Autowired
    private PostalItemRepository postalItemRepository;

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @Autowired
    private TrackStatusMapper statusMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private TrackStatus testTrackStatus;

    @Container
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("dbname")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        var postalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(postalItem);

        var postOffice = Instancio.of(modelGenerator.getPostOfficeModel())
                .create();
        postOfficeRepository.save(postOffice);

        testTrackStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        testTrackStatus.setPostalItem(postalItem);
        testTrackStatus.setPostOffice(postOffice);
        statusRepository.save(testTrackStatus);
    }

    @AfterEach
    public void cleanup() {
        statusRepository.deleteAll();
        postalItemRepository.deleteAll();
        postOfficeRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1.0/statuses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1.0/statuses/" + testTrackStatus.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("postalId").asString().isEqualTo(testTrackStatus.getPostalItem().getPostalId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var postalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(postalItem);
        testTrackStatus.setPostalItem(postalItem);
        var dto = statusMapper.map(testTrackStatus);

        RequestBuilder request = post("/api/v1.0/statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("id").isNotNull(),
                v -> v.node("postalId").asString().isEqualTo(testTrackStatus.getPostalItem().getPostalId())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = new TrackStatusUpdateDTO();
        var postalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(postalItem);
        dto.setPostalId(JsonNullable.of(postalItem.getPostalId()));

        RequestBuilder request = put("/api/v1.0/statuses/" + testTrackStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("id").isEqualTo(testTrackStatus.getId()),
                v -> v.node("postalId").asString().isEqualTo(dto.getPostalId().get())
        );
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/v1.0/statuses/" + testTrackStatus.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(statusRepository.existsById(testTrackStatus.getId())).isEqualTo(false);
    }
}
