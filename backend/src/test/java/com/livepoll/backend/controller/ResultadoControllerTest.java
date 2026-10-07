package com.livepoll.backend.controller;

import com.livepoll.backend.dto.ResultadoEnqueteResponse;
import com.livepoll.backend.dto.ResultadoOpcaoResponse;
import com.livepoll.backend.service.ResultadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultadoController.class)
public class ResultadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResultadoService resultadoService;

    @Test
    void deveRetornarResultadosComSucesso() throws Exception {
        Long enqueteId = 1L;

        ResultadoEnqueteResponse resposta =
                new ResultadoEnqueteResponse(
                        enqueteId,
                        "Qual linguagem você prefere?",
                        true,
                        List.of(
                                new ResultadoOpcaoResponse(
                                        2L,
                                        "Java",
                                        7L
                                ),
                                new ResultadoOpcaoResponse(
                                        3L,
                                        "Python",
                                        3L
                                )
                        )
                );

        when(resultadoService.buscarResultados(enqueteId))
                .thenReturn(resposta);

        mockMvc.perform(
                        get("/api/v1/enquetes/{id}/resultados", enqueteId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enqueteId").value(1))
                .andExpect(jsonPath("$.titulo")
                        .value("Qual linguagem você prefere?"))
                .andExpect(jsonPath("$.aberta").value(true))
                .andExpect(jsonPath("$.opcoes[0].opcaoId").value(2))
                .andExpect(jsonPath("$.opcoes[0].textoOpcao")
                        .value("Java"))
                .andExpect(jsonPath("$.opcoes[0].votos").value(7))
                .andExpect(jsonPath("$.opcoes[1].opcaoId").value(3))
                .andExpect(jsonPath("$.opcoes[1].textoOpcao")
                        .value("Python"))
                .andExpect(jsonPath("$.opcoes[1].votos").value(3));
    }
}
