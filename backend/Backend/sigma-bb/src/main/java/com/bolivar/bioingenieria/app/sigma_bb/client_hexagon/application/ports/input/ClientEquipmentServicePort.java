package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada que define las operaciones de negocio disponibles
 * para la gestión de {@link ClientEquipment}.
 *
 * <p>Esta interfaz establece el contrato que debe implementar el servicio
 * de aplicación para consultar, crear, actualizar y eliminar equipos de cliente.</p>
 */
public interface ClientEquipmentServicePort {

    /**
     * Busca un {@link ClientEquipment} por su identificador único.
     *
     * @param clientEquipmentId identificador del {@link ClientEquipment} a consultar
     * @return {@link ClientEquipment} encontrado
     */
    ClientEquipment findByID(UUID clientEquipmentId);

    /**
     * Obtiene todos los {@link ClientEquipment} registrados.
     *
     * @return lista de {@link ClientEquipment}
     */
    List<ClientEquipment> findAll();

    /**
     * Crea y almacena un {@link ClientEquipment}.
     *
     * @param clientEquipment datos del {@link ClientEquipment} a persistir
     * @return {@link ClientEquipment} almacenado
     */
    ClientEquipment save(ClientEquipment clientEquipment);

    /**
     * Actualiza la información de un {@link ClientEquipment} existente.
     *
     * @param clientEquipmentId identificador del {@link ClientEquipment} a actualizar
     * @param clientEquipment datos nuevos del {@link ClientEquipment}
     * @return {@link ClientEquipment} actualizado
     */
    ClientEquipment update(UUID clientEquipmentId, ClientEquipment clientEquipment);

    /**
     * Elimina (marca como inactivo) un {@link ClientEquipment} del sistema.
     *
     * @param clientEquipmentId identificador del {@link ClientEquipment} a eliminar
     */
    void delete(UUID clientEquipmentId);
}
