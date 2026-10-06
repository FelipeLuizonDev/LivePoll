package com.livepoll.backend.controller;

import com.livepoll.backend.dto.EnqueteRequest;
import com.livepoll.backend.dto.EnqueteResponse;
import com.livepoll.backend.service.EnqueteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/enquetes")
public class EnqueteController {

    private final EnqueteService enqueteService;

    public EnqueteController(EnqueteService enqueteService) {
        this.enqueteService = enqueteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnqueteResponse criar(
            @Valid @RequestBody EnqueteRequest request
            ) {
        return enqueteService.criar(request);
    }

    @GetMapping
    public List<EnqueteResponse> listar() {
        return enqueteService.listar();
    }

    @GetMapping("/{id}")
    public EnqueteResponse buscarPorId(@PathVariable Long id) {
        return enqueteService.buscarPorId(id);
    }

    @PatchMapping("/{id}/abrir")
    public EnqueteResponse abrir(@PathVariable Long id) {
        return enqueteService.abrir(id);
    }

    @PatchMapping("/{id}/fechar")
    public EnqueteResponse fechar(@PathVariable Long id) {
        return enqueteService.fechar(id);
    }
}
