package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio que representa una sede o sucursal de un cliente.
 * Contiene la información de ubicación, identificadores de referencias
 * y la lista de áreas de servicio asociadas a la sede.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Headquarter {
    /**
     * Identificador único de la sede.
     * Tipo: {@link UUID}
     */
    private UUID identificadorSede;

    /**
     * Nombre o denominación de la sede.
     */
    private String nombreSede;

    /**
     * Nombre de la calle en la dirección de la sede.
     */
    private String direccionCalleSede;

    /**
     * Nombre de la carrera en la dirección de la sede.
     */
    private String direccionCarreraSede;

    /**
     * Número de dirección de la sede.
     */
    private String direccionNumeroSede;

    /**
     * Indica si la sede está activa en el sistema.
     * true: sede en operación; false: sede inactiva o cerrada.
     */
    private boolean estadoActivo;

    /**
     * Lista de áreas de servicio asociadas a esta sede.
     * Tipo: {@link List} de {@link ServiceArea}
     */
    private List<ServiceArea> serviceAreaList;

    /**
     * Identificador único de la ciudad donde se ubica la sede.
     * Tipo: {@link UUID}
     */
    private String identificadorCiudad;

    /**
     * Identificador único del cliente al que pertenece la sede.
     * Tipo: {@link UUID}
     */
    private UUID identificadorCliente;

    /**
     * Lista de encargados responsables de la sede.
     * Tipo: {@link List} de {@link Manager}
     */
    private List<Manager> managerList;

    // ----------------------------------------------------------------------
    // ------------- Métodos para gestionar areas de servicio  --------------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link ServiceArea} a la lista de las areas de servicio de la sede.
     *
     * @param serviceArea Objeto {@link ServiceArea} a agregar
     */
    public void addServiceArea(ServiceArea serviceArea) {
        this.serviceAreaList.add(serviceArea);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link ServiceArea} de la lista de areas de servicio.
     *
     * @param idServiceArea {@link UUID} de {@link ServiceArea} a eliminar
     */
    public void removeServiceArea(UUID idServiceArea) {
        this.serviceAreaList.stream()
                .filter(e -> e.getIdentificadorAreaServicio().equals(idServiceArea))
                .findFirst()
                .ifPresent(e -> e.setEstadoActivo(false));
    }

    // ----------------------------------------------------------------------
    // ----------- Métodos para gestionar encargados de la sede  ------------
    // ----------------------------------------------------------------------

    /**
    * Agrega un {@link Manager} a la lista de encargados de la sede.
    *
    * @param manager Objeto {@link Manager} a agregar
    */
    public void addManager(Manager manager) {
        this.managerList.add(manager);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link Manager} de la lista de encargados de la sede.
     *
     * @param idManager {@link UUID} de {@link Manager} a eliminar
     */
    public void removeManager(UUID idManager) {
        this.managerList.stream()
                .filter(m -> m.getIdentificadorEncargado().equals(idManager))
                .findFirst()
                .ifPresent(m -> m.setEstadoActivo(false));
    }
}
