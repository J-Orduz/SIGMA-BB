package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.TechnicalVerificationEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TechnicalVerificationPersistenceMapper {

    TechnicalVerification toTechnicalVerification(TechnicalVerificationEntity technicalVerificationEntity);

    TechnicalVerificationEntity toTechnicalVerificationEntity(TechnicalVerification technicalVerification);

    List<TechnicalVerification> toTechnicalVerificationList(List<TechnicalVerificationEntity> technicalVerificationEntities);

    @Mapping(target = "technicalVerificationId", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "verificationType", source = "verificationType")
    TechnicalVerificationEquipment toTechnicalVerificationEquipment(TechnicalVerificationEntity technicalVerificationEntity);

    List<TechnicalVerificationEquipment> toTechnicalVerificationEquipmentList(List<TechnicalVerificationEntity> technicalVerificationEntities);

    @Mapping(target = "id", source = "technicalVerificationId")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "verificationType", source = "verificationType")
    @Mapping(target = "active", ignore = true)
    TechnicalVerificationEntity toTechnicalVerificationEntity(TechnicalVerificationEquipment technicalVerificationEquipment);

    List<TechnicalVerificationEntity> toTechnicalVerificationEntityList(List<TechnicalVerificationEquipment> technicalVerificationEquipmentList);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(TechnicalVerification source, @org.mapstruct.MappingTarget TechnicalVerificationEntity target);
}
