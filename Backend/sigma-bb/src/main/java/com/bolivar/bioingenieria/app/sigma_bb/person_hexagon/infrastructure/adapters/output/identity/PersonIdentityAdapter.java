package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.identity;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity.PersonIdentityPort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity.response.PersonIdentityResponse;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.RoleType;
import com.sun.codemodel.JStringLiteral;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador de salida para la gestión de identidad del usuario utilizando Keycloak.
 * Implementa el puerto de salida PersonIdentityPort y se encarga de interactuar con Keycloak para crear usuarios con diferentes roles.
 */
@Component
@RequiredArgsConstructor
public class PersonIdentityAdapter implements PersonIdentityPort {

    private final Keycloak keycloakClient;

    /**
     * Crea un nuevo usuario con rol de ingeniero en Keycloak a partir de la información proporcionada en el DTO de respuesta.
     *
     * @param personIdentityResponse DTO que contiene la información del usuario a crear
     * @return UUID del usuario creado en Keycloak
     */
    @Override
    public String createUser(PersonIdentityResponse personIdentityResponse, RoleType roleType) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(personIdentityResponse.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(personIdentityResponse.getUserName());
        user.setEmail(personIdentityResponse.getEmail());
        user.setFirstName(personIdentityResponse.getFirstName());
        user.setLastName(personIdentityResponse.getLastName());

        switch(roleType) {
            case ENGINEER -> user.setGroups(List.of("engineers"));
            case CEO_CLIENT -> user.setGroups(List.of("clients"));
            case ADMIN -> user.setGroups(List.of("admins"));
        }

        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(List.of(credential));

        Response response = keycloakClient
                .realm("sigma-bb-realm")
                .users()
                .create(user);

        if (response.getStatus() == 201) {
            return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        } else {
            throw new RuntimeException("Error al crear ingeniero en Keycloak: " + response.getStatus());
        }
    }

    @Override
    public void deleteUser(String userId) {
        try {
            keycloakClient
                    .realm("sigma-bb-realm")
                    .users()
                    .delete(userId);

        }catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario en Keycloak: " + e.getMessage());
        }
    }

    @Override
    public UUID createSuperAdminUser(PersonIdentityResponse personIdentityResponse) {
        return null;
    }
}
