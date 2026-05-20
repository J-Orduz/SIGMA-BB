package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {
    private String reportId;
    private EquipmentDTO equipment;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EquipmentDTO {
        private UUID id;
        private EquipmentTypeDTO equipmentType;
        private BrandDTO brand;
        private ModelDTO model;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EquipmentTypeDTO {
        private UUID id;
        private String name;
        private String technicalDefinition;
        private String careRecommendations;
        private Integer voltage;
        private BigDecimal amperage;
        private String predominantTechnology;
        private Boolean verifiable;
        private Long unitMaintenanceValue;
        private List<MetrologicalDataDTO> metrologicalData;
        private List<UUID> verificationIds;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetrologicalDataDTO {
        private BigDecimal value;
        private String type;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BrandDTO {
        private UUID id;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModelDTO {
        private UUID id;
        private String invima;
        private ManufacturerDTO manufacturer;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ManufacturerDTO {
        private String id;
        private String name;
        private CountryDTO country;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CountryDTO {
        private String id;
        private String name;
    }
}
