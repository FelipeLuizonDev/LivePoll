package com.livepoll.backend.controller;

import com.livepoll.backend.dto.ResultadoEnqueteResponse;
import com.livepoll.backend.service.ResultadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enquetes")
public class ResultadoController {

    private final ResultadoService resultadoService;

    public ResultadoController(ResultadoService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping("/{id}/resultados")
    public ResultadoEnqueteResponse buscarResultados(@PathVariable Long id) {
        return resultadoService.buscarResultados(id);
    }
}
