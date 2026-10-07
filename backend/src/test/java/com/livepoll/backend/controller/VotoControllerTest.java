package com.livepoll.backend.controller;

import com.livepoll.backend.dto.VotoRequest;
import com.livepoll.backend.exception.EnqueteNotFoundException;
import com.livepoll.backend.service.VotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotoController.class)
public class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VotoService votoService;

    @Test
    void deveRegistrarVotoComSucesso() throws Exception {
        Long enqueteId = 1L;

        VotoRequest request = new VotoRequest(2L);

        doNothing().when(votoService).votar(enqueteId, request);

        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", enqueteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoOpcaoNaoForInformada() throws Exception {
        Long enqueteId = 1L;

        String jsonInvalido = """
                {
                    "opcaoId": null
                }
                """;

        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", enqueteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoEnqueteNaoExiste() throws Exception {
        Long enqueteId = 999L;

        VotoRequest request = new VotoRequest(2L);

        doThrow(new EnqueteNotFoundException(enqueteId))
                .when(votoService)
                .votar(enqueteId, request);

        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", enqueteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoIdDaEnqueteForInvalido() throws Exception {
        VotoRequest request = new VotoRequest(2L);

        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }
}
