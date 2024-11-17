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
import org.springframework.boot.test.mock.mockito.MockBean;
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
import shamshaev.code.component.DataInitializer;
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.model.PostalItem;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.repository.PostalItemRepository;
import shamshaev.code.repository.TrackStatusRepository;
import shamshaev.code.util.ModelGenerator;
import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PostalItemControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostalItemRepository postalItemRepository;

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @Autowired
    private TrackStatusRepository trackStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @MockBean
    private DataInitializer dataInitializer;

    private PostalItem testPostalItem;

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

        testPostalItem = Instancio.of(modelGenerator.getPostalItemModel())
                .create();
        postalItemRepository.save(testPostalItem);
    }

    @AfterEach
    public void cleanup() {
        trackStatusRepository.deleteAll();
        postalItemRepository.deleteAll();
        postOfficeRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1.0/postal_items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        var postOffice = Instancio.of(modelGenerator.getPostOfficeModel())
                .create();
        postOfficeRepository.save(postOffice);

        var trackStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        trackStatus.setPostalItem(testPostalItem);
        trackStatus.setPostOffice(postOffice);
        trackStatusRepository.save(trackStatus);

        MvcResult result = mockMvc.perform(get("/api/v1.0/postal_items/" + testPostalItem.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("recipientName").isEqualTo(testPostalItem.getRecipientName())
        );
        assertThat(response.getContentAsString()).contains(trackStatus.getPostOffice().getPostCode());
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getPostalItemModel())
                .create();

        RequestBuilder request = post("/api/v1.0/postal_items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        var postalItem = postalItemRepository.findByPostalId(data.getPostalId())
                .orElseThrow(() -> new ResourceNotFoundException("PostalItem with postalId '"
                        + data.getPostalId() + "' not found"));
        assertNotNull(postalItem);
        assertThat(postalItem.getPostalId()).isEqualTo(data.getPostalId());
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = new PostalItemUpdateDTO();
        dto.setRecipientName(JsonNullable.of("updated_name"));

        RequestBuilder request = put("/api/v1.0/postal_items/" + testPostalItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        var postalItem = postalItemRepository.findById(testPostalItem.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PostalItem with id '"
                        + testPostalItem.getId() + "' not found"));
        assertThat(postalItem.getRecipientName()).isEqualTo(dto.getRecipientName().get());
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/v1.0/postal_items/" + testPostalItem.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(postalItemRepository.existsById(testPostalItem.getId())).isEqualTo(false);
    }
}
