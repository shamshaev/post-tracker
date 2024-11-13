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
import org.springframework.test.context.ActiveProfiles;
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
import shamshaev.code.dto.StatusUpdateDTO;
import shamshaev.code.mapper.StatusMapper;
import shamshaev.code.model.Status;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.repository.PostalItemRepository;
import shamshaev.code.repository.StatusRepository;
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
@ActiveProfiles("test")
class StatusControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private PostalItemRepository postalItemRepository;

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @Autowired
    private StatusMapper statusMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Status testStatus;

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

        testStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        testStatus.setPostalItem(postalItem);
        testStatus.setPostOffice(postOffice);
        statusRepository.save(testStatus);
    }

    @AfterEach
    public void cleanup() {
        statusRepository.deleteAll();
        postalItemRepository.deleteAll();
        postOfficeRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/statuses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/statuses/" + testStatus.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("postalId").asString().isEqualTo(testStatus.getPostalItem().getPostalId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var postalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(postalItem);
        testStatus.setPostalItem(postalItem);
        var dto = statusMapper.map(testStatus);

        RequestBuilder request = post("/api/statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("id").isNotNull(),
                v -> v.node("postalId").asString().isEqualTo(testStatus.getPostalItem().getPostalId())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = new StatusUpdateDTO();
        var postalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(postalItem);
        dto.setPostalId(JsonNullable.of(postalItem.getPostalId()));

        RequestBuilder request = put("/api/statuses/" + testStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("id").isEqualTo(testStatus.getId()),
                v -> v.node("postalId").asString().isEqualTo(dto.getPostalId().get())
        );
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/statuses/" + testStatus.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(statusRepository.existsById(testStatus.getId())).isEqualTo(false);
    }
}
