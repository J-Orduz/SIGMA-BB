package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request;

import lombok.*;

/**
 * DTO de solicitud para la creación de un teléfono asociado a una persona.
 * Contiene el número telefónico que se desea asociar a la persona en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhonePersonCreateRequestUseCase {

    /**
     * Número telefónico de la persona.
     */
    private String telefonoPersona;
}
