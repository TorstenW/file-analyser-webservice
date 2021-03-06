package net.wmann.fileanalyser.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.wmann.fileanalyser.dto.ErrorListDto;
import net.wmann.fileanalyser.dto.EvaluationDto;
import net.wmann.fileanalyser.model.Error;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"task.pool.nthread=4"})
public class ControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void evaluateTestFile() throws Exception {
        EvaluationDto expectedResult = getValidEvaluationDto();

        String responseContent = mockMvc.perform(get("/evaluation?url=http://localhost:" + port + "/examplefile/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals("Wrong evaluation result returned by controller", expectedResult, (mapper.readValue(responseContent, EvaluationDto.class)));
    }

    @Test
    public void evaluateMultipleTestFile() throws Exception {
        EvaluationDto expectedResult = getValidEvaluationDto();

        String responseContent = mockMvc.perform(get("/evaluation?url=http://localhost:" + port + "/examplefile/valid&url=http://localhost:" + port + "/examplefile/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals("Wrong evaluation result returned by controller", expectedResult, (mapper.readValue(responseContent, EvaluationDto.class)));
    }

    @Test
    public void evaluateNoUrl() throws Exception {
        EvaluationDto expectedResult = new EvaluationDto();

        String responseContent = mockMvc.perform(get("/evaluation?url="))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals("Wrong evaluation result returned by controller", expectedResult, (mapper.readValue(responseContent, EvaluationDto.class)));
    }

    @Test
    public void evaluateNoParameter() throws Exception {
        mockMvc.perform(get("/evaluation"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void evaluateInvalidUrl() throws Exception {
        Error error = new Error("InvalidUrlException", null, "The following URLs are not valid: invalidURL123");
        ErrorListDto expectedResult = new ErrorListDto(error);

        String responseContent = mockMvc.perform(get("/evaluation?url=invalidURL123"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals("Wrong evaluation result returned by controller", expectedResult, (mapper.readValue(responseContent, ErrorListDto.class)));
    }

    @Test
    public void evaluateInvalidTestFile() throws Exception {
        Error error1 = new Error("File processing error for URL: http://localhost:" + port + "/examplefile/invalid"
                                , "InvalidLineException"
                                , "Line: 'Bernhard Belling, Kohlesubventionen'");
        ErrorListDto expectedResult = new ErrorListDto(error1);

        String responseContent = mockMvc.perform(get("/evaluation?url=http://localhost:" + port + "/examplefile/invalid"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseContent);

        Assert.assertEquals("Wrong evaluation result returned by controller", expectedResult, (mapper.readValue(responseContent, ErrorListDto.class)));
    }

    private EvaluationDto getValidEvaluationDto() {
        EvaluationDto evaluationDto = new EvaluationDto();
        evaluationDto.setLeastWordy("Caesare Collins");
        evaluationDto.setMostSecurity("Alexander Abel");
        return evaluationDto;
    }

}
