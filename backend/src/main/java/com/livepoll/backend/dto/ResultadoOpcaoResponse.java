package com.livepoll.backend.dto;

public record ResultadoOpcaoResponse(
        Long opcaoId,
        String textoOpcao,
        Long votos
) {
}
