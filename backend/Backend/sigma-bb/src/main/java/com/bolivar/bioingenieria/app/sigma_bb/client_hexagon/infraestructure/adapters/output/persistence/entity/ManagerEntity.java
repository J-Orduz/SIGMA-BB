package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad JPA que representa a un encargado o responsable dentro del sistema.
 * Contiene información sobre su identificador, tipo de encargado, estado de actividad,
 * y las relaciones con el área de servicio y la sede a la que está asignado.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "encargado")
public class ManagerEntity {

    /**
     * Identificador único del encargado.
     * Tipo: String (puede ser un UUID o cualquier otro formato de identificador).
     */
    @Id
    @Column(name = "k_identificador", nullable = false, unique = true)
    private UUID identificadorEncargado;

    /**
     * Tipo o rol del encargado dentro del sistema (por ejemplo, "Jefe de Área", "Supervisor", etc.).
     * No puede ser nulo ni estar en blanco.
     */
    @Column(name = "t_tipo_encargado", nullable = false)
    @NotBlank
    private String tipoEncargado;

    /**
     * Indica si el encargado está activo en el sistema (soft-delete).
     * true: encargado en actividad; false: encargado inactivo o desactivado.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo;

    /**
     * Relación ManyToOne con el {@link ServiceAreaEntity} a la que está asignado el encargado.
     * Puede ser nulo si el encargado no está asignado a ningún área de servicio.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_area_servicio", nullable = true)
    private ServiceAreaEntity serviceArea;

    /**
     * Relación ManyToOne con el {@link HeadquarterEntity} a la que está asignado el encargado.
     * Puede ser nulo si el encargado no está asignado a ninguna sede.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_sede", nullable = true)
    private HeadquarterEntity headquarter;

}
