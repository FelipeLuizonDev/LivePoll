package com.livepoll.backend.service;

import com.livepoll.backend.dto.ResultadoEnqueteResponse;
import com.livepoll.backend.dto.ResultadoOpcaoResponse;
import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.exception.EnqueteNotFoundException;
import com.livepoll.backend.repository.EnqueteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultadoService {

    private final EnqueteRepository enqueteRepository;
    private final RedisService redisService;
    private final VotoService votoService;

    public ResultadoService(
            EnqueteRepository enqueteRepository,
            RedisService redisService,
            VotoService votoService
    ) {
        this.enqueteRepository = enqueteRepository;
        this.redisService = redisService;
        this.votoService = votoService;
    }

    @Transactional(readOnly = true)
    public ResultadoEnqueteResponse buscarResultados(Long enqueteId) {

        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EnqueteNotFoundException(enqueteId));

        if(!redisService.existeContador(enqueteId)) {
            votoService.reconstruirContadores(enqueteId);
        }

        List<ResultadoOpcaoResponse> resultados = enquete.getOpcoes()
                .stream()
                .map(opcao -> criarResultadoOpcao(enqueteId, opcao))
                .toList();

        return new ResultadoEnqueteResponse(
                enquete.getId(),
                enquete.getTitulo(),
                enquete.getAberta(),
                resultados
        );
    }

    private ResultadoOpcaoResponse criarResultadoOpcao(
            Long enqueteId,
            Opcao opcao
    ) {
        String votos = redisService.buscarVotos(
                enqueteId,
                opcao.getId()
        );

        return new ResultadoOpcaoResponse(
                opcao.getId(),
                opcao.getTextoOpcao(),
                Long.valueOf(votos)
        );
    }
}
