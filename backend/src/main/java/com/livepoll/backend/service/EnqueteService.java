package com.livepoll.backend.service;

import com.livepoll.backend.dto.EnqueteRequest;
import com.livepoll.backend.dto.EnqueteResponse;
import com.livepoll.backend.dto.OpcaoResponse;
import com.livepoll.backend.entity.Enquete;
import com.livepoll.backend.entity.Opcao;
import com.livepoll.backend.exception.EnqueteNotFoundException;
import com.livepoll.backend.repository.EnqueteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class EnqueteService {

    private final EnqueteRepository enqueteRepository;

    public EnqueteService(EnqueteRepository enqueteRepository) {
        this.enqueteRepository = enqueteRepository;
    }

    @Transactional
    public EnqueteResponse criar(EnqueteRequest request) {

        validarOpcoesDuplicadas(request.opcoes());

        Enquete enquete = new Enquete(request.titulo());

        for (String textoOpcao : request.opcoes()) {
            Opcao opcao = new Opcao(textoOpcao);
            enquete.adicionarOpcao(opcao);
        }

        Enquete salva = enqueteRepository.save(enquete);

        return converterParaResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<EnqueteResponse> listar() {

        return enqueteRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnqueteResponse buscarPorId(Long id) {

        Enquete enquete = enqueteRepository.findById(id)
                .orElseThrow(() ->
                    new EnqueteNotFoundException(id)
                );

        return converterParaResponse(enquete);
    }

    @Transactional
    public EnqueteResponse abrir(Long id) {

        Enquete enquete = enqueteRepository.findById(id)
                .orElseThrow(() ->
                        new EnqueteNotFoundException(id)
                );

        enquete.setAberta(true);

        return converterParaResponse(enquete);
    }

    @Transactional
    public EnqueteResponse fechar(Long id) {

        Enquete enquete = enqueteRepository.findById(id)
                .orElseThrow(() ->
                        new EnqueteNotFoundException(id)
                );

        enquete.setAberta(false);

        return converterParaResponse(enquete);
    }

    private EnqueteResponse converterParaResponse(Enquete enquete) {

        List<OpcaoResponse> opcoes = enquete.getOpcoes()
                .stream()
                .map(opcao -> new OpcaoResponse(
                        opcao.getId(),
                        opcao.getTextoOpcao()
                ))
                .toList();

        return new EnqueteResponse(
                enquete.getId(),
                enquete.getTitulo(),
                enquete.getAberta(),
                opcoes
        );
    }

    private void validarOpcoesDuplicadas(List<String> opcoes) {

        Set<String> opcoesNormalizadas = new HashSet<>();

        for (String opcao : opcoes) {
            String opcaoNormalizada = opcao
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (!opcoesNormalizadas.add(opcaoNormalizada)) {
                throw new IllegalArgumentException(
                        "A enquete não pode possuir opções duplicadas"
                );
            }
        }
    }
}
