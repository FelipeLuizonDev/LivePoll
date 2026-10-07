package com.livepoll.backend.integration;

import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.repository.EnqueteRepository;
import com.livepoll.backend.repository.OpcaoRepository;
import com.livepoll.backend.repository.VotoRepository;
import com.livepoll.backend.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VotoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnqueteRepository enqueteRepository;

    @Autowired
    private OpcaoRepository opcaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void limparDados() {
        redisService.limparContadores(1L);
        votoRepository.deleteAll();
        opcaoRepository.deleteAll();
        enqueteRepository.deleteAll();
    }

    @Test
    void deveRegistrarVotoNoPostgresEIncrementarRedis()
            throws Exception {

        Enquete enquete = new Enquete(
                "Qual linguagem você prefere?"
        );

        Opcao opcao1 = new Opcao("Java");
        Opcao opcao2 = new Opcao("Python");

        enquete.adicionarOpcao(opcao1);
        enquete.adicionarOpcao(opcao2);

        enquete = enqueteRepository.save(enquete);

        Long enqueteId = enquete.getId();
        Long opcaoId = opcao1.getId();

        String json = objectMapper.writeValueAsString(
                Map.of("opcaoId", opcaoId)
        );

        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", enqueteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        assertEquals(1, votoRepository.count());

        assertEquals(
                "1",
                redisService.buscarVotos(enqueteId, opcaoId)
        );
    }

    @Test
    void deveReconstruirRedisAoConsultarResultados() throws Exception {
        Enquete enquete = new Enquete(
                "Qual linguagem você prefere?"
        );

        Opcao opcao1 = new Opcao("Java");
        Opcao opcao2 = new Opcao("Python");

        enquete.adicionarOpcao(opcao1);
        enquete.adicionarOpcao(opcao2);

        enquete = enqueteRepository.save(enquete);

        Long enqueteId = enquete.getId();
        Long opcaoId = opcao1.getId();

        // Cria o voto diretamente no PostgreSQL
        com.livepoll.backend.entity.Voto voto =
                new com.livepoll.backend.entity.Voto(opcao1);

        votoRepository.save(voto);

        // Garante que o Redis não possui o contador
        redisService.limparContadores(enqueteId);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/v1/enquetes/{id}/resultados", enqueteId)
                )
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .jsonPath("$.opcoes[0].votos")
                                .value(1)
                );

        assertEquals(
                "1",
                redisService.buscarVotos(enqueteId, opcaoId)
        );
    }

    @Test
    void deveRegistrarMultiplosVotosNaMesmaOpcao() throws Exception {
        Enquete enquete = new Enquete(
                "Qual linguagem você prefere?"
        );

        Opcao opcao1 = new Opcao("Java");
        Opcao opcao2 = new Opcao("Python");

        enquete.adicionarOpcao(opcao1);
        enquete.adicionarOpcao(opcao2);

        enquete = enqueteRepository.save(enquete);

        Long enqueteId = enquete.getId();
        Long opcaoId = opcao1.getId();

        String json = objectMapper.writeValueAsString(
                Map.of("opcaoId", opcaoId)
        );

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(
                    post("/api/v1/enquetes/{id}/votos", enqueteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            ).andExpect(status().isOk());
        }

        assertEquals(3, votoRepository.count());

        assertEquals(
                "3",
                redisService.buscarVotos(enqueteId, opcaoId)
        );
    }

    @Test
    void deveManterContadoresIndependentesPorOpcao() throws Exception {
        Enquete enquete = new Enquete(
                "Qual linguagem você prefere?"
        );

        Opcao opcao1 = new Opcao("Java");
        Opcao opcao2 = new Opcao("Python");

        enquete.adicionarOpcao(opcao1);
        enquete.adicionarOpcao(opcao2);

        enquete = enqueteRepository.save(enquete);

        Long enqueteId = enquete.getId();
        Long opcao1Id = opcao1.getId();
        Long opcao2Id = opcao2.getId();

        String votoOpcao1 = objectMapper.writeValueAsString(
                Map.of("opcaoId", opcao1Id)
        );

        String votoOpcao2 = objectMapper.writeValueAsString(
                Map.of("opcaoId", opcao2Id)
        );

        // 2 votos em Java
        for (int i = 0; i < 2; i++) {
            mockMvc.perform(
                    post("/api/v1/enquetes/{id}/votos", enqueteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(votoOpcao1)
            ).andExpect(status().isOk());
        }

        // 1 voto em Python
        mockMvc.perform(
                post("/api/v1/enquetes/{id}/votos", enqueteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(votoOpcao2)
        ).andExpect(status().isOk());

        assertEquals(3, votoRepository.count());

        assertEquals(
                "2",
                redisService.buscarVotos(enqueteId, opcao1Id)
        );

        assertEquals(
                "1",
                redisService.buscarVotos(enqueteId, opcao2Id)
        );
    }
}
