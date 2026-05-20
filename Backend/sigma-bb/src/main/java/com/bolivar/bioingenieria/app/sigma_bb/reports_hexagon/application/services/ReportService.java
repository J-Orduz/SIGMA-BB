package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.report.EquipmentDomainReportDTO;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.report.CountryDomainReportDTO;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response.ReportResponseDTO;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.input.ReportDataProviderPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportDataProviderPort<EquipmentDomainReportDTO> equipmentReportProvider;
    private final ReportDataProviderPort<CountryDomainReportDTO> countryReportProvider;

    @Autowired
    public ReportService(
            @Qualifier("equipmentReportProvider") ReportDataProviderPort<EquipmentDomainReportDTO> equipmentReportProvider,
            @Qualifier("countryReportProvider") ReportDataProviderPort<CountryDomainReportDTO> countryReportProvider) {
        this.equipmentReportProvider = equipmentReportProvider;
        this.countryReportProvider = countryReportProvider;
    }

    public ReportResponseDTO generateReport(String reportId, String modelId) {
        var equipmentData = equipmentReportProvider.provideReportData(modelId);
        if (equipmentData == null) {
            return ReportResponseDTO.builder()
                    .reportId(reportId)
                    .build();
        }

        String countryId = null;
        if (equipmentData.getManufacturer() != null) {
            countryId = equipmentData.getManufacturer().getCountryId();
        }

        var countryData = (countryId != null)
                ? countryReportProvider.provideReportData(countryId)
                : null;

        return ReportResponseDTO.builder()
                .reportId(reportId)
                .equipment(toEquipmentReport(equipmentData, countryData, null))
                .build();
    }

    private ReportResponseDTO.EquipmentDTO toEquipmentReport(EquipmentDomainReportDTO equipmentData,
                                                            CountryDomainReportDTO countryData,
                                                            List<UUID> verificationIds) {
        var equipment = equipmentData.getEquipment();
        var equipmentType = equipmentData.getEquipmentType();
        var brand = equipmentData.getBrand();
        var model = equipmentData.getModel();
        var manufacturer = equipmentData.getManufacturer();
        var country = countryData != null ? countryData.getCountry() : null;

        ReportResponseDTO.CountryDTO countryDto = null;
        if (country != null || (manufacturer != null && manufacturer.getCountryId() != null)) {
            countryDto = ReportResponseDTO.CountryDTO.builder()
                    .id(country != null ? country.getId() : manufacturer.getCountryId())
                    .name(country != null ? country.getName() : null)
                    .build();
        }

        ReportResponseDTO.ManufacturerDTO manufacturerDto = null;
        if (manufacturer != null) {
            manufacturerDto = ReportResponseDTO.ManufacturerDTO.builder()
                    .id(manufacturer.getId())
                    .name(manufacturer.getName())
                    .country(countryDto)
                    .build();
        }

        ReportResponseDTO.ModelDTO modelDto = null;
        if (model != null) {
            modelDto = ReportResponseDTO.ModelDTO.builder()
                    .id(model.getId())
                    .invima(model.getInvima())
                    .manufacturer(manufacturerDto)
                    .build();
        }

        ReportResponseDTO.BrandDTO brandDto = null;
        if (brand != null) {
            brandDto = ReportResponseDTO.BrandDTO.builder()
                    .id(brand.getId())
                    .name(brand.getName())
                    .build();
        }

        ReportResponseDTO.EquipmentTypeDTO equipmentTypeDto = null;
        if (equipmentType != null) {
            List<ReportResponseDTO.MetrologicalDataDTO> metrologicalData = null;
            if (equipmentType.getMetrologicalData() != null) {
                metrologicalData = equipmentType.getMetrologicalData().stream()
                        .map(item -> ReportResponseDTO.MetrologicalDataDTO.builder()
                                .value(item.getValue())
                                .type(item.getType())
                                .build())
                        .toList();
            }

            equipmentTypeDto = ReportResponseDTO.EquipmentTypeDTO.builder()
                    .id(equipmentType.getId())
                    .name(equipmentType.getEquipmentTypeName())
                    .technicalDefinition(equipmentType.getTechnicalDefinition())
                    .careRecommendations(equipmentType.getCareRecommendations())
                    .voltage(equipmentType.getVoltage())
                    .amperage(equipmentType.getAmperage())
                    .predominantTechnology(equipmentType.getPredominantTechnology())
                    .verifiable(equipmentType.getVerifiable())
                    .unitMaintenanceValue(equipmentType.getUnitMaintenanceValue())
                    .metrologicalData(metrologicalData)
                    .verificationIds(toVerificationIds(verificationIds))
                    .build();
        }

        return ReportResponseDTO.EquipmentDTO.builder()
                .id(equipment != null ? equipment.getId() : null)
                .equipmentType(equipmentTypeDto)
                .brand(brandDto)
                .model(modelDto)
                .build();
    }

    private List<UUID> toVerificationIds(List<UUID> verificationIds) {
        if (verificationIds == null) {
            return null;
        }

        return verificationIds.stream().toList();
    }
}
