package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.listeners;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.*;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.*;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.report.EquipmentDomainReportDTO;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.input.ReportDataProviderPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("equipmentReportProvider")
public class EquipmentReportProviderAdapter implements ReportDataProviderPort<EquipmentDomainReportDTO> {

    private final BrandServicePort brandServicePort;
    private final ManufacturerServicePort manufacturerServicePort;
    private final EquipmentServicePort equipmentServicePort;
    private final EquipmentTypeServicePort equipmentTypeServicePort;
    private final ModelServicePort modelServicePort;

    private final EquipmentRestMapper equipmentRestMapper;
    private final EquipmentTypeRestMapper equipmentTypeRestMapper;
    private final BrandRestMapper brandRestMapper;
    private final ModelRestMapper modelRestMapper;
    private final ManufacturerRestMapper manufacturerRestMapper;

    @Autowired
    public EquipmentReportProviderAdapter(BrandServicePort brandServicePort,
                                          ManufacturerServicePort manufacturerServicePort,
                                          EquipmentServicePort equipmentServicePort,
                                          EquipmentTypeServicePort equipmentTypeServicePort,
                                          ModelServicePort modelServicePort,
                                          EquipmentRestMapper equipmentRestMapper,
                                          EquipmentTypeRestMapper equipmentTypeRestMapper,
                                          BrandRestMapper brandRestMapper,
                                          ModelRestMapper modelRestMapper,
                                          ManufacturerRestMapper manufacturerRestMapper) {
        this.brandServicePort = brandServicePort;
        this.manufacturerServicePort = manufacturerServicePort;
        this.equipmentServicePort = equipmentServicePort;
        this.equipmentTypeServicePort = equipmentTypeServicePort;
        this.modelServicePort = modelServicePort;
        this.equipmentRestMapper = equipmentRestMapper;
        this.equipmentTypeRestMapper = equipmentTypeRestMapper;
        this.brandRestMapper = brandRestMapper;
        this.modelRestMapper = modelRestMapper;
        this.manufacturerRestMapper = manufacturerRestMapper;
    }

    @Override
    public String domainName() {
        return "equipment-domain";
    }

    @Override
    public EquipmentDomainReportDTO provideReportData(String modelId) {
        var model = modelServicePort.findById(modelId);
        var equipment = equipmentServicePort.findById(model.getEquipmentId().toString());
        var equipmentType = equipmentTypeServicePort.findById(equipment.getEquipmentTypeId().toString());
        var brand = brandServicePort.findById(equipment.getBrandId().toString());
        var manufacturer = manufacturerServicePort.findById(model.getManufacturerId().toString());

        var eqResp = equipmentRestMapper.toEquipmentResponse(equipment);
        eqResp.setEquipmentTypeResponse(null);
        eqResp.setBrandResponse(null);

        return EquipmentDomainReportDTO.builder()
                .equipment(eqResp)
                .equipmentType(equipmentTypeRestMapper.toEquipmentTypeResponse(equipmentType))
                .brand(brandRestMapper.toBrandResponse(brand))
                .model(modelRestMapper.toModelResponse(model))
                .manufacturer(manufacturerRestMapper.toManufacturerResponse(manufacturer))
                .build();
    }
}
