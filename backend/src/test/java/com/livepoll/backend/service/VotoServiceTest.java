package com.livepoll.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.livepoll.backend.dto.VotoRequest;
import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.entity.Voto;
import com.livepoll.backend.exception.EnqueteNotFoundException;
import com.livepoll.backend.repository.EnqueteRepository;
import com.livepoll.backend.repository.OpcaoRepository;
import com.livepoll.backend.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private EnqueteRepository enqueteRepository;

    @Mock
    private OpcaoRepository opcaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private VotoService votoService;

    @Test
    void deveRegistrarVotoComSucesso() {

        Long enqueteId = 1L;
        Long opcaoId = 2L;

        Enquete enquete = new Enquete(
                "Qual linguagem você prefere?"
        );

        ReflectionTestUtils.setField(
                enquete,
                "id",
                enqueteId
        );

        Opcao opcao = new Opcao("Java");

        ReflectionTestUtils.setField(
                opcao,
                "id",
                opcaoId
        );

        enquete.adicionarOpcao(opcao);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(opcaoRepository.findById(opcaoId))
                .thenReturn(Optional.of(opcao));

        VotoRequest request = new VotoRequest(opcaoId);

        votoService.votar(enqueteId, request);

        verify(votoRepository)
                .save(any(Voto.class));

        verify(redisService)
                .incrementarVoto(
                        enqueteId,
                        opcaoId
                );
    }

    @Test
    void naoDeveRegistrarVotoEmEnqueteFechada() {
        Long enqueteId = 1L;
        Long opcaoId = 2L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);
        enquete.setAberta(false);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        VotoRequest request = new VotoRequest(opcaoId);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.votar(enqueteId, request)
        );

        assertEquals("A enquete está fechada", exception.getMessage());

        verify(votoRepository, never()).save(any(Voto.class));
        verify(redisService, never()).incrementarVoto(anyLong(), anyLong());
    }

    @Test
    void naoDeveRegistrarVotoQuandoEnqueteNaoExiste() {
        Long enqueteId = 999L;
        Long opcaoId = 2L;

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.empty());

        VotoRequest request = new VotoRequest(opcaoId);

        EnqueteNotFoundException exception = assertThrows(
                EnqueteNotFoundException.class,
                () -> votoService.votar(enqueteId, request)
        );

        assertEquals(
                "Enquete não encontrada: " + enqueteId,
                exception.getMessage()
        );

        verify(votoRepository, never()).save(any(Voto.class));
        verify(redisService, never()).incrementarVoto(anyLong(), anyLong());
    }

    @Test
    void naoDeveRegistrarVotoQuandoOpcaoNaoExiste() {
        Long enqueteId = 1L;
        Long opcaoId = 999L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(opcaoRepository.findById(opcaoId))
                .thenReturn(Optional.empty());

        VotoRequest request = new VotoRequest(opcaoId);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.votar(enqueteId, request)
        );

        assertEquals(
                "Opção não encontrada: " + opcaoId,
                exception.getMessage()
        );

        verify(votoRepository, never()).save(any(Voto.class));
        verify(redisService, never()).incrementarVoto(anyLong(), anyLong());
    }

    @Test
    void naoDeveRegistrarVotoQuandoOpcaoNaoPertenceAEnquete() {
        Long enqueteId = 1L;
        Long outraEnqueteId = 99L;
        Long opcaoId = 2L;

        Enquete enquete = new Enquete("Qual linguagem você prefere?");
        ReflectionTestUtils.setField(enquete, "id", enqueteId);

        Enquete outraEnquete = new Enquete("Qual banco você prefere?");
        ReflectionTestUtils.setField(outraEnquete, "id", outraEnqueteId);

        Opcao opcao = new Opcao("PostgreSQL");
        ReflectionTestUtils.setField(opcao, "id", opcaoId);
        opcao.setEnquete(outraEnquete);

        when(enqueteRepository.findById(enqueteId))
                .thenReturn(Optional.of(enquete));

        when(opcaoRepository.findById(opcaoId))
                .thenReturn(Optional.of(opcao));

        VotoRequest request = new VotoRequest(opcaoId);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.votar(enqueteId, request)
        );

        assertEquals(
                "A opção não pertence à enquete informada",
                exception.getMessage()
        );

        verify(votoRepository, never()).save(any(Voto.class));
        verify(redisService, never()).incrementarVoto(anyLong(), anyLong());
    }
}
