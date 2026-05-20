package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio que representa un área de servicio dentro de una sede de cliente.
 * Contiene información sobre el área, sus equipos asociados y el encargado responsable.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceArea {

    /**
     * Identificador único del área de servicio.
     * Tipo: {@link UUID}
     */
    private UUID identificadorAreaServicio;

    /**
     * Nombre o denominación del área de servicio.
     */
    private String nombreAreaServicio;

    /**
     * Indica si el área de servicio está activa en el sistema.
     * true: área en operación; false: área inactiva o desactivada.
     */
    private boolean estadoActivo;

    /**
     * Lista de equipos de cliente asociados a esta área de servicio.
     * Tipo: {@link List} de {@link ClientEquipment}
     */
    private List<ClientEquipment> clientEquipmentList;

    /**
     * Identificador único de la sede a la que pertenece esta área de servicio.
     * Tipo: {@link UUID}
     */
    private UUID identificadorSede;

    /**
     * Encargado responsable del área de servicio.
     * Tipo: {@link Manager}
     */
    private List<Manager> managerList;

    // ----------------------------------------------------------------------
    // ------------- Métodos para gestionar Equipos cliente  --------------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link ClientEquipment} a la lista de equipos cliente de esta área.
     *
     * @param clientEquipment Objeto {@link ClientEquipment} a agregar
     */
    public void addServiceArea(ClientEquipment clientEquipment) {
        this.clientEquipmentList.add(clientEquipment);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link ClientEquipment} de la lista de equipos cliente.
     *
     * @param idClientEquipment {@link UUID} de {@link ClientEquipment} a eliminar
     */
    public void removeServiceArea(UUID idClientEquipment) {
        this.clientEquipmentList.stream()
                .filter(e -> e.getIdentificadorEquipoCliente().equals(idClientEquipment))
                .findFirst()
                .ifPresent(e -> e.setEstadoActivo(false));
    }

    // ----------------------------------------------------------------------
    // ------- Métodos para gestionar encargados del área de servicio  ------
    // ----------------------------------------------------------------------

    /**
     * Agrega un {@link Manager} a la lista de encargados responsables del área de servicio.
     *
     * @param manager Objeto {@link Manager} a agregar
     */
    public void addManager(Manager manager) {
        this.managerList.add(manager);
    }

    /**
     * Elimina (cambia su estadoActivo a false) un {@link Manager} de la lista de encargados del área de servicio.
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
