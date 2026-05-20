package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringTechnicalVerificationRepository extends JpaRepository<TechnicalVerificationEntity, UUID> {
    List<TechnicalVerificationEntity> findAllByIdIn(List<UUID> ids);
}
