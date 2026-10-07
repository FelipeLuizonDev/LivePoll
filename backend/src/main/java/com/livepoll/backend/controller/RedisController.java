package com.livepoll.backend.controller;

import com.livepoll.backend.service.RedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/teste")
    public String salvar() {

        redisService.salvar(
                "livepoll:test",
                "Redis funcionando"
        );

        return "Valor salvo no Redis";
    }

    @GetMapping("/teste")
    public String buscar() {
        return redisService.buscar("livepoll:test");
    }

    @PostMapping("/teste/voto/{enqueteId}/{opcaoId}")
    public Long votar(
            @PathVariable Long enqueteId,
            @PathVariable Long opcaoId
    ) {
        return redisService.incrementarVoto(enqueteId, opcaoId);
    }

    @GetMapping("/teste/voto/{enqueteId}/{opcaoId}")
    public String consultarVotos(
            @PathVariable Long enqueteId,
            @PathVariable Long opcaoId
    ) {
        return redisService.buscarVotos(enqueteId, opcaoId);
    }
}
