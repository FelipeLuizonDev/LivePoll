package com.livepoll.backend.controller;

import com.livepoll.backend.dto.VotoRequest;
import com.livepoll.backend.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enquetes")
public class VotoController {

    private final VotoService votoService;

    public VotoController(
            VotoService votoService
    ) {
        this.votoService = votoService;
    }

    @PostMapping("/{id}/votos")
    public ResponseEntity<Void> votar(
            @PathVariable Long id,
            @Valid @RequestBody VotoRequest request
    ) {
        votoService.votar(id, request);

        return ResponseEntity.ok().build();
    }
}
