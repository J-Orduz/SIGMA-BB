package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponse {
    private String id;
    private String name;
}
