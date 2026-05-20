package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.TechnicalVerificationEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEquipmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TechnicalVerificationEquipmentMapper {

    @Mapping(target = "technicalVerificationId", source = "tecnicalVerificationEquipmentId.verificationId")
    TechnicalVerificationEquipment toTechnicalVerificationEquipment(TechnicalVerificationEquipmentEntity entity);

    @Mapping(target = "tecnicalVerificationEquipmentId.verificationId", source = "technicalVerificationId")
    @Mapping(target = "tecnicalVerificationEquipmentId.typeEquipmentId", ignore = true)
    @Mapping(target = "equipmentTypeEntity", ignore = true)
    @Mapping(target = "technicalVerificationEntity", ignore = true)
    @Mapping(target = "active", ignore = true)
    TechnicalVerificationEquipmentEntity toTechnicalVerificationEquipmentEntity(TechnicalVerificationEquipment domain);

    List<TechnicalVerificationEquipment> toTechnicalVerificationEquipmentList(List<TechnicalVerificationEquipmentEntity> entities);

    @Mapping(target = "tecnicalVerificationEquipmentId", ignore = true)
    @Mapping(target = "equipmentTypeEntity", ignore = true)
    @Mapping(target = "technicalVerificationEntity", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDomain(TechnicalVerificationEquipment source,
                                @MappingTarget TechnicalVerificationEquipmentEntity target);
}
