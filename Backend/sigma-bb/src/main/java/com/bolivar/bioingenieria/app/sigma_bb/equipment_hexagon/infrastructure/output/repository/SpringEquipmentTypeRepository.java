package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.EquipmentTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringEquipmentTypeRepository extends JpaRepository<EquipmentTypeEntity, UUID> {
    boolean existsById(@NonNull UUID id);

    @Query("select distinct e from EquipmentTypeEntity e left join fetch e.metrologicalDataEntities")
    List<EquipmentTypeEntity> findAllWithMetrologicalData();

    @Query("select e from EquipmentTypeEntity e left join fetch e.metrologicalDataEntities where e.id = :id")
    Optional<EquipmentTypeEntity> findByIdWithMetrologicalData(@Param("id") UUID id);
}
