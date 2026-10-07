package com.livepoll.backend.repository;

import com.livepoll.backend.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query("""
            SELECT v.opcao.id, COUNT(v.id)
            FROM Voto v
            WHERE v.opcao.enquete.id = :enqueteId
            GROUP BY v.opcao.id
            """)
    List<Object[]> contarVotosPorOpcao(Long enqueteId);
}
