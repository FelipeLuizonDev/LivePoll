package com.livepoll.backend.repository;

import com.livepoll.backend.entity.Enquete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnqueteRepository extends JpaRepository<Enquete, Long> {
}
