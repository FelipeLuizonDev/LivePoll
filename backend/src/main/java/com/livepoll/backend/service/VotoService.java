package com.livepoll.backend.service;

import com.livepoll.backend.dto.VotoRequest;
import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.entity.Voto;
import com.livepoll.backend.exception.EnqueteNotFoundException;
import com.livepoll.backend.repository.EnqueteRepository;
import com.livepoll.backend.repository.OpcaoRepository;
import com.livepoll.backend.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VotoService {

    private final EnqueteRepository enqueteRepository;
    private final OpcaoRepository opcaoRepository;
    private final VotoRepository votoRepository;
    private final RedisService redisService;

    public VotoService(
            EnqueteRepository enqueteRepository,
            OpcaoRepository opcaoRepository,
            VotoRepository votoRepository,
            RedisService redisService
    ) {
        this.enqueteRepository = enqueteRepository;
        this.opcaoRepository = opcaoRepository;
        this.votoRepository = votoRepository;
        this.redisService = redisService;
    }

    @Transactional
    public void votar(Long enqueteId, VotoRequest request) {

        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EnqueteNotFoundException(enqueteId));

        if (!enquete.getAberta()) {
            throw new IllegalArgumentException("A enquete está fechada");
        }

        Opcao opcao = opcaoRepository.findById(request.opcaoId())
                .orElseThrow(() -> new IllegalArgumentException("Opção não encontrada: " + request.opcaoId()));

        if (!opcao.getEnquete().getId().equals(enqueteId)) {
            throw new IllegalArgumentException("A opção não pertence à enquete informada");
        }

        Voto voto = new Voto(opcao);

        votoRepository.save(voto);

        redisService.incrementarVoto(enqueteId, opcao.getId());
    }

    @Transactional(readOnly = true)
    public void reconstruirContadores(Long enqueteId) {

        enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EnqueteNotFoundException(enqueteId));

        redisService.limparContadores(enqueteId);

        List<Object[]> resultados = votoRepository.contarVotosPorOpcao(enqueteId);

        for (Object[] resultado : resultados) {

            Long opcaoId = (Long) resultado[0];
            Long quantidade = (Long) resultado[1];

            redisService.definirVotos(enqueteId, opcaoId, quantidade);
        }
    }
}
