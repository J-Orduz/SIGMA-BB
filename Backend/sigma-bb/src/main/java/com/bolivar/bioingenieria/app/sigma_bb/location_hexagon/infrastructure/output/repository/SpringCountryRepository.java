package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringCountryRepository extends JpaRepository<CountryEntity, String> {
}
