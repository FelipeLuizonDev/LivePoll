package com.livepoll.backend.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void salvar(String chave, String valor) {
        redisTemplate.opsForValue().set(chave, valor);
    }

    public String buscar(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }

    public Long incrementarVoto(Long enqueteId, Long opcaoId) {
        String chave = "livepoll:enquete:" + enqueteId + ":votos";
        String campo = "opcao:" + opcaoId;

        return redisTemplate.opsForHash()
                .increment(chave, campo, 1);
    }

    public String buscarVotos(Long enqueteId, Long opcaoId) {
        String chave = "livepoll:enquete:" + enqueteId + ":votos";
        String campo = "opcao:" + opcaoId;

        Object valor = redisTemplate.opsForHash()
                .get(chave, campo);

        return valor != null ? valor.toString() : "0";
    }

    public void limparContadores(Long enqueteId) {
        String chave = "livepoll:enquete:" + enqueteId + ":votos";

        redisTemplate.delete(chave);
    }

    public void definirVotos(
            Long enqueteId,
            Long opcaoId,
            Long quantidade
    ) {
        String chave = "livepoll:enquete:" + enqueteId + ":votos";
        String campo = "opcao:" + opcaoId;

        redisTemplate.opsForHash()
                .put(chave, campo, quantidade.toString());
    }
}
