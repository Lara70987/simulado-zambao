package com.socorro.simulado.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socorro.simulado.entity.Curso;
import com.socorro.simulado.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void setup() {
        cursoRepository.deleteAll();
    }

    @Test
    void shouldCreateListAndDeleteCursoFlow() throws Exception {
        Curso curso = new Curso();
        curso.setNome("Java Spring");
        curso.setDescricao("Curso de Java");

        String createdJson = mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Java Spring"))
                .andExpect(jsonPath("$.deleted").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Curso created = objectMapper.readValue(createdJson, Curso.class);

        mockMvc.perform(get("/cursos")
                        .param("nome", "Ja"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Java Spring"));

        mockMvc.perform(delete("/cursos/{id}", created.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
