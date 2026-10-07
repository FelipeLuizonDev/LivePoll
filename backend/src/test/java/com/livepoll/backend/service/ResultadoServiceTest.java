package com.livepoll.backend.service;

import com.livepoll.backend.dto.ResultadoEnqueteResponse;
import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.repository.EnqueteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock
    private EnqueteRepository enqueteRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private ResultadoService resultadoService;

    @Test
    void deveRetornarResultadosCorretamente() {
        Long enqueteId = 1L;
        Long opcao1Id = 2L;
        Long opcao2Id = 3L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);

        Opcao opcao1 = new Opcao("Java");
        ReflectionTestUtils.setField(opcao1, "id", opcao1Id);

        Opcao opcao2 = new Opcao("Python");
        ReflectionTestUtils.setField(opcao2, "id", opcao2Id);

        enquete.adicionarOpcao(opcao1);
        enquete.adicionarOpcao(opcao2);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(redisService.existeContador(enqueteId))
                .thenReturn(true);

        when(redisService.buscarVotos(enqueteId, opcao1Id))
                .thenReturn("7");

        when(redisService.buscarVotos(enqueteId, opcao2Id))
                .thenReturn("3");

        ResultadoEnqueteResponse resultado =
                resultadoService.buscarResultados(enqueteId);

        assertEquals(enqueteId, resultado.enqueteId());
        assertEquals(
                "Qual linguagem você prefere?",
                resultado.titulo()
        );
        assertEquals(true, resultado.aberta());

        assertEquals(2, resultado.opcoes().size());

        assertEquals(opcao1Id, resultado.opcoes().get(0).opcaoId());
        assertEquals("Java", resultado.opcoes().get(0).textoOpcao());
        assertEquals(7L, resultado.opcoes().get(0).votos());

        assertEquals(opcao2Id, resultado.opcoes().get(1).opcaoId());
        assertEquals("Python", resultado.opcoes().get(1).textoOpcao());
        assertEquals(3L, resultado.opcoes().get(1).votos());

        verify(votoService, never()).reconstruirContadores(enqueteId);
    }

    @Test
    void deveReconstruirRedisQuandoContadorNaoExiste() {
        Long enqueteId = 1L;
        Long opcaoId = 2L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);

        Opcao opcao = new Opcao("Java");
        ReflectionTestUtils.setField(opcao, "id", opcaoId);

        enquete.adicionarOpcao(opcao);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(redisService.existeContador(enqueteId))
                .thenReturn(false);

        when(redisService.buscarVotos(enqueteId, opcaoId))
                .thenReturn("5");

        ResultadoEnqueteResponse resultado =
                resultadoService.buscarResultados(enqueteId);

        verify(votoService).reconstruirContadores(enqueteId);

        assertEquals(5L, resultado.opcoes().get(0).votos());
    }

    @Test
    void naoDeveReconstruirRedisQuandoContadorExiste() {
        Long enqueteId = 1L;
        Long opcaoId = 2L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);

        Opcao opcao = new Opcao("Java");
        ReflectionTestUtils.setField(opcao, "id", opcaoId);

        enquete.adicionarOpcao(opcao);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(redisService.existeContador(enqueteId))
                .thenReturn(true);

        when(redisService.buscarVotos(enqueteId, opcaoId))
                .thenReturn("8");

        ResultadoEnqueteResponse resultado =
                resultadoService.buscarResultados(enqueteId);

        assertEquals(8L, resultado.opcoes().get(0).votos());

        verify(votoService, never())
                .reconstruirContadores(enqueteId);

        verify(redisService)
                .buscarVotos(enqueteId, opcaoId);
    }
}