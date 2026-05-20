package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;

import java.util.List;
import java.util.Optional;

public interface TechnicalVerificationPersistencePort {
    List<TechnicalVerification> findAll();
    Optional<TechnicalVerification> findById(String id);
    TechnicalVerification save(TechnicalVerification technicalVerification);
    TechnicalVerification update(String id, TechnicalVerification technicalVerification);
    void delete(String id);
    List<TechnicalVerification> findAllById(List<String> ids);
}
