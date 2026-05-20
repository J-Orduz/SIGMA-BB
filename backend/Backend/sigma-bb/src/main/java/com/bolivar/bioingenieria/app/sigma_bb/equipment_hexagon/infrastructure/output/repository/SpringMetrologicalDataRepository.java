package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.MetrologicalDataEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.MetrologicalDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringMetrologicalDataRepository extends JpaRepository<MetrologicalDataEntity, MetrologicalDataId> {
}
