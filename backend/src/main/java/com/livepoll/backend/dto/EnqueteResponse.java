package com.livepoll.backend.dto;

import java.util.List;

public record EnqueteResponse(
        Long id,
        String titulo,
        Boolean aberta,
        List<OpcaoResponse> opcoes
) {
}
