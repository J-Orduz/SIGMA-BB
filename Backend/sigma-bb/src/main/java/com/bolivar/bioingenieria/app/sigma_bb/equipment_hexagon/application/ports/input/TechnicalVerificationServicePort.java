package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.TechnicalVerificationPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.CreateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.DeleteTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.UpdateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;

import java.util.List;

public interface TechnicalVerificationServicePort {
    List<TechnicalVerification> findAll();
    TechnicalVerification findById(String id);
    TechnicalVerification save(CreateTechnicalVerificationCommand command);
    TechnicalVerification update(String id, UpdateTechnicalVerificationCommand command);
    void delete(DeleteTechnicalVerificationCommand command);
    TechnicalVerification patchUpdate(String id, TechnicalVerificationPatchCommand command);
}
