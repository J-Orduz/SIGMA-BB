package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.TechnicalVerificationEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.TechnicalVerificationRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.TechnicalVerificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TechnicalVerificationRestMapper {
    TechnicalVerification toTechnicalVerification(TechnicalVerificationRequest technicalVerificationRequest);
    TechnicalVerificationResponse toTechnicalVerificationResponse(TechnicalVerification technicalVerification);
    TechnicalVerificationResponse toTechnicalVerificationResponse(TechnicalVerificationEquipment technicalVerificationEquipment);
    List<TechnicalVerificationResponse> toTechnicalVerificationResponseList(List<TechnicalVerification> technicalVerificationList);
    List<TechnicalVerificationResponse> toTechnicalVerificationResponseListFromEquipment(List<TechnicalVerificationEquipment> technicalVerificationList);
}
