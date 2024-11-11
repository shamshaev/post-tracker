package shamshaev.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
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
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.PostalItemMapper;
import shamshaev.code.model.PostalItem;
import shamshaev.code.repository.PostalItemRepository;
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
@Transactional
class PostalItemControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostalItemRepository postalItemRepository;

    @Autowired
    private PostalItemMapper postalItemMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelGenerator modelGenerator;

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

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/postal_items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/postal_items/" + testPostalItem.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("recipientName").isEqualTo(testPostalItem.getRecipientName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getPostalItemModel())
                .create();

        RequestBuilder request = post("/api/postal_items")
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

        RequestBuilder request = put("/api/postal_items/" + testPostalItem.getId())
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
        mockMvc.perform(delete("/api/postal_items/" + testPostalItem.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(postalItemRepository.existsById(testPostalItem.getId())).isEqualTo(false);
    }
}
