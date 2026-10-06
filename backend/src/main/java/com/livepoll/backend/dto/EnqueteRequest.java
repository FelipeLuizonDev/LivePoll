package com.livepoll.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EnqueteRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres")
        String titulo,

        @Size(min = 2, message = "A enquete deve possuir pelo menos 2 opções")
        List<
                @NotBlank(message = "A opção não pode ser vazia")
                String
                > opcoes
) {
}
