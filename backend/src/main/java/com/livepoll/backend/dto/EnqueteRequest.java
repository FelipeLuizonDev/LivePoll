package com.livepoll.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EnqueteRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres")
        String titulo,

        @NotEmpty(message = "A enquete deve possuir opções")
        @Size(min = 2, max = 10, message = "A enquete deve possuir entre 2 e 10 opções")
        List<
                @NotBlank(message = "A opção não pode ser vazia")
                @Size(max = 100, message = "Cada opção pode ter no máximo 100 caracteres")
                String
                > opcoes
) {
}
