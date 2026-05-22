package com.example.identity.docs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Tagged {@code openapi} so {@code ./gradlew openapi} emits {@code openapi.json} for CI / Redocly.
 */
@Tag("openapi")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiExportTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void writeOpenApiDocument() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        byte[] body = result.getResponse().getContentAsByteArray();
        Path dir = Path.of(System.getProperty("openapi.export.dir", "build/openapi"));
        Files.createDirectories(dir);
        Files.write(dir.resolve("openapi.json"), body);
    }
}
