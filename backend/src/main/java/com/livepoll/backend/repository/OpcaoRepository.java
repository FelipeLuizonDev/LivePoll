package com.livepoll.backend.repository;

import com.livepoll.backend.entity.Opcao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcaoRepository extends JpaRepository<Opcao, Long> {
}
