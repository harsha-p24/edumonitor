package com.sdlms.lab;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabPcRepository extends JpaRepository<LabPc, Long> {
    List<LabPc> findByLabId(Long labId);
    Optional<LabPc> findByHostname(String hostname);
}
