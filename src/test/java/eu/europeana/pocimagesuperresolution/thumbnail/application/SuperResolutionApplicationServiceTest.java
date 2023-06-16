package eu.europeana.pocimagesuperresolution.thumbnail.application;

import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.json.JSONObject;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class SuperResolutionApplicationServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThumbnailEnhancer enhancer;

    @Test
    void givenFileUrlToEnhance_thenSuccess() throws Exception {
        // given
        final String expectedOutput = "file://output.extension";
        when(enhancer.enhance(anyString())).thenReturn(expectedOutput);
        // when -> then
        JSONObject jsonObject = new JSONObject(new HashMap() {{ put("input","file://input.extension"); }});
        this.mockMvc.perform(post("/enhance")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedOutput)));
    }
}
