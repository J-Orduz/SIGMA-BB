package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.headquarter_response.HeadquarterResponse;
import lombok.*;

import java.util.List;

/**
 * DTO de entrada para crear un nuevo {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.
 * Contiene los datos básicos del cliente y listas opcionales de teléfonos y correos.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    /**
     * Identificador único del cliente.
     * Normalmente es el NIT o el código fiscal/tributario que identifica al cliente en el negocio.
     * @see #tipoIdentifiacion
     */
    private String identificadorCliente;

    /**
     * Tipo de identificación del cliente.
     * Valores esperados: por ejemplo "NIT", "Cédula", "Pasaporte".
     */
    private String tipoIdentifiacion;

    /**
     * Razón social o nombre comercial del cliente.
     */
    private String razonSocial;

    /**
     * Identificador del país del cliente.
     */
    private String identificadorPais;

    /**
     * Indica si el cliente está activo en el sistema.
     * Usado para filtrado lógico (soft-delete) y para determinar si el cliente puede ser seleccionado en operaciones comerciales.
     */
    private boolean estadoActivo;

    /**
     * Lista de teléfonos asociados al cliente.
     * Cada elemento es un {@link PhoneClientResponse} con los datos del teléfono a almacenar.
     */
    private List<PhoneClientResponse> telefonosCliente;

    /**
     * Lista de correos electrónicos asociados al cliente.
     * Cada elemento es un {@link EmailClientResponse} con los datos del correo a almacenar.
     */
    private List<EmailClientResponse> correosCliente;

    /**
    * Lista de sedes o sucursales (headquarters) asociadas al cliente.
    * Cada elemento es un {@link HeadquarterResponse} con los datos de la sede a almacenar.
    */
    private List<HeadquarterResponse> headquarterClientList;
}
