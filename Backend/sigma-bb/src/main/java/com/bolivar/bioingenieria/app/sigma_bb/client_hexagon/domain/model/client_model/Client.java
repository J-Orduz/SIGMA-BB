package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio que representa un cliente (empresa o persona jurídica).
 *
 * Contiene los datos principales necesarios para identificar y gestionar
 * a un cliente dentro del dominio del módulo "client_hexagon".
 *
 * Campos clave:
 * - identificadorCliente: identificador único del cliente (string, podría ser NIT o similar)
 * - tipoIdentifiacion: tipo de documento o categoría de identificación
 * - razonSocial: nombre o razón social del cliente
 * - estadoActivo: si el cliente está activo en el sistema
 * - correosCliente / telefonosCliente: contactos asociados
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {

    /**
     * Identificador único del cliente. Puede ser un NIT, código o clave definida por el negocio.
     */
    private String identificadorCliente;

    /**
     * Tipo de identificación asociado al cliente (por ejemplo: NIT, Cédula, Pasaporte).
     */
    private String tipoIdentifiacion;

    /**
     * Razón social o nombre comercial del cliente.
     */
    private String razonSocial;

    /**
     * Indica si el cliente está activo en el sistema. Usado para filtrado lógico (soft-delete).
     */
    private boolean estadoActivo;

    /**
     * Identificador del país asociado al cliente. Puede ser un código ISO o similar.
     */
    private String identificadorPais;

    /**
     * Cédula del representante legal asociado al cliente.
     */
    private String identificadorRepresentante;

    /**
     * Lista de {@link EmailClient} asociados al cliente.
     */
    private List<EmailClient> emailClientList;

    /**
     * Lista de {@link PhoneClient} asociados al cliente.
     */
    private List<PhoneClient> phoneClientList;

    /**
     * Lista de {@link Headquarter} asociados al cliente.
     */
    private List<Headquarter> headquarterList;

    // ----------------------------------------------------------------------
    // ------------- Métodos para gestionar correos y teléfonos -------------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link EmailClient} a la lista de correos de la persona.
     *
     * @param email Objeto {@link EmailClient} a agregar
     */
    public void addEmail(EmailClient email) {
        this.emailClientList.add(email);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link EmailClient} de la lista de correos del cliente.
     *
     * @param idEmail UUID de {@link EmailClient} a eliminar
     */
    public void removeEmail(UUID idEmail) {
        this.emailClientList.stream()
                .filter(e -> e.getIdentificadorCorreoCliente().equals(idEmail))
                .findFirst()
                .ifPresent(e -> e.setEstadoActivo(false));
    }

    /**
     * Agrega un {@link PhoneClient} a la lista de teléfonos del cliente.
     *
     * @param phone Objeto {@link PhoneClient} a agregar
     */
    public void addPhone(PhoneClient phone) {
        this.phoneClientList.add(phone);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link PhoneClient} de la lista de teléfonos del cliente.
     *
     * @param idPhone UUID de {@link PhoneClient} a eliminar
     */
    public void removePhone(UUID idPhone) {
        this.phoneClientList.stream()
                .filter(p -> p.getIdentificadorTelefonoCliente().equals(idPhone))
                .findFirst()
                .ifPresent(p -> p.setEstadoActivo(false));
    }

    // ----------------------------------------------------------------------
    // ------------- Métodos para gestionar sedes  --------------------------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link Headquarter} a la lista de sedes.
     *
     * @param headquarter Objeto {@link Headquarter} a agregar
     */
    public void addHeadquarter(Headquarter headquarter) {
         this.headquarterList.add(headquarter);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link Headquarter} de la lista de sedes.
     *
     * @param idHeadquarter UUID de {@link Headquarter} a eliminar
     */
    public void removeHeadquarter(UUID idHeadquarter) {
        this.headquarterList.stream()
                .filter(e -> e.getIdentificadorSede().equals(idHeadquarter))
                .findFirst()
                .ifPresent(e -> e.setEstadoActivo(false));
    }
}
