package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request;

import lombok.*;

/**
 * DTO de solicitud para la creación de un correo electrónico asociado a una persona.
 * Contiene la dirección de correo electrónico que se desea asociar a la persona en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailPersonCreateRequestUseCase {
    /**
     * Dirección de correo electrónico de la persona.
     */
    private String correoPersona;
}
