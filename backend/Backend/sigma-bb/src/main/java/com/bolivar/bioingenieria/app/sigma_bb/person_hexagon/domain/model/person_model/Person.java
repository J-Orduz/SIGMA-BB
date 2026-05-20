package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio que representa una persona en el sistema.
 * Contiene la información personal, así como sus teléfonos y correos electrónicos asociados.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {

    /**
     * Identificador único de la persona.
     */
    private UUID identificador;

    /**
     * Número de cédula de la persona.
     */
    private String cedula;

    /**
     * Primer nombre de la persona.
     */
    private String primerNombre;

    /**
     * Segundo nombre de la persona.
     */
    private String segundoNombre;

    /**
     * Primer apellido de la persona.
     */
    private String primerApellido;

    /**
     * Segundo apellido de la persona.
     */
    private String segundoApellido;

    /**
     * Tipo principal de persona.
     */
    private String tipoPersona;

    /**
     * Tipo secundario de persona, si aplica.
     */
    private String segundoTipoPersona;

    /**
     * Indica si la persona está activa en el sistema.
     */
    private boolean estadoActivo;

    /**
     * Lista de {@link EmailPerson} asociados a la persona.
     */
    private List<EmailPerson> emailPersonList;

    /**
     * Lista de {@link PhonePerson} a la persona.
     */
    private List<PhonePerson> phonePersonList;

    // ----------------------------------------------------------------------
    // ------------- Métodos para gestionar correos y teléfonos -------------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link EmailPerson} a la lista de correos de la persona.
     *
     * @param email Objeto {@link EmailPerson} a agregar
     */
    public void addEmail(EmailPerson email) {
        this.emailPersonList.add(email);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link EmailPerson} de la lista de correos de la persona.
     *
     * @param idEmail UUID de {@link EmailPerson} a eliminar
     */
    public void removeEmail(UUID idEmail) {
        this.emailPersonList.stream()
                .filter(e -> e.getIdCorreoPersona().equals(idEmail))
                .findFirst()
                .ifPresent(e -> e.setEstadoActivo(false));
    }

    /**
     * Agrega un {@link PhonePerson} a la lista de teléfonos de la persona.
     *
     * @param phone Objeto {@link PhonePerson} a agregar
     */
    public void addPhone(PhonePerson phone) {
        this.phonePersonList.add(phone);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link PhonePerson} de la lista de teléfonos de la persona.
     *
     * @param idPhone UUID de {@link PhonePerson} a eliminar
     */
    public void removePhone(UUID idPhone) {
        this.phonePersonList.stream()
                .filter(p -> p.getIdTelefonoPersona().equals(idPhone))
                .findFirst()
                .ifPresent(p -> p.setEstadoActivo(false));
    }


}
