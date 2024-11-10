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
import shamshaev.code.dto.PostOfficeUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.PostOfficeMapper;
import shamshaev.code.model.PostOffice;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.util.ModelGenerator;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class PostOfficeControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @Autowired
    private PostOfficeMapper postOfficeMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private PostOffice testPostOffice;

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

        testPostOffice = Instancio.of(modelGenerator.getPostOfficeModel())
                .create();
        postOfficeRepository.save(testPostOffice);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/post_offices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/post_offices/" + testPostOffice.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("name").isEqualTo(testPostOffice.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getPostOfficeModel())
                .create();

        RequestBuilder request = post("/api/post_offices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        var postOffice = postOfficeRepository.findByPostCode(data.getPostCode())
                .orElseThrow(() -> new ResourceNotFoundException("PostOffice with postcode '"
                        + data.getPostCode() + "' not found"));
        assertNotNull(postOffice);
        assertThat(postOffice.getPostCode()).isEqualTo(data.getPostCode());
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = new PostOfficeUpdateDTO();
        dto.setName(JsonNullable.of("updated_name"));

        RequestBuilder request = put("/api/post_offices/" + testPostOffice.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        var postOffice = postOfficeRepository.findById(testPostOffice.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PostOffice with id '"
                        + testPostOffice.getId() + "' not found"));
        assertThat(postOffice.getName()).isEqualTo(dto.getName().get());
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/post_offices/" + testPostOffice.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(postOfficeRepository.existsById(testPostOffice.getId())).isEqualTo(false);
    }
}
