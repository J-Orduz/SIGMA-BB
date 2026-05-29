package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity.response.PersonIdentityResponse;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.RoleType;

import java.util.UUID;

/**
 * Puerto de salida para la gestión de identidad del usuario.
 * Define las operaciones relacionadas con la creación y gestión de identidades del usuario en el sistema.
 */
public interface PersonIdentityPort {

    /**
     * Crea un nuevo usuario con el rol especificado en el sistema a partir de la información proporcionada en el DTO de respuesta.
     * @param personIdentityResponse DTO que contiene la información del usuario a crear
     * @param roleType tipo de rol que se asignará al usuario (ENGINEER, ADMIN, CEO_CLIENT)
     * @return UUID del usuario creado
     */
    String createUser(PersonIdentityResponse personIdentityResponse, RoleType roleType);

    /**
     * Elimina un usuario del sistema a partir de su UUID.
     * @param userId UUID del usuario a eliminar
     */
    void deleteUser(String userId);
    /**
     * crea un nuevo usuario con rol de super administrador en el sistema a partir de la información proporcionada en el DTO de respuesta.
     * @param personIdentityResponse
     * @return UUID del usuario creado
     */
    UUID createSuperAdminUser(PersonIdentityResponse personIdentityResponse);
}
