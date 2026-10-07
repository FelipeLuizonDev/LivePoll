package com.livepoll.backend.dto;

import jakarta.validation.constraints.NotNull;

public record VotoRequest(

        @NotNull(message = "O ID da opção é obrigatório")
        Long opcaoId
) {
}
