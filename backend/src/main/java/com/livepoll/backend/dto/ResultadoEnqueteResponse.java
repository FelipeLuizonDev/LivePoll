package com.livepoll.backend.dto;

import java.util.List;

public record ResultadoEnqueteResponse(
        Long enqueteId,
        String titulo,
        Boolean aberta,
        List<ResultadoOpcaoResponse> opcoes
) {
}
