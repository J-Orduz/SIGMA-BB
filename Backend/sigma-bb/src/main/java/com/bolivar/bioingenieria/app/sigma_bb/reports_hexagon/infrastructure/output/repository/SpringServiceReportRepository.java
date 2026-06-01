package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.entities.ServiceReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringServiceReportRepository extends JpaRepository<ServiceReportEntity, UUID> {
}
