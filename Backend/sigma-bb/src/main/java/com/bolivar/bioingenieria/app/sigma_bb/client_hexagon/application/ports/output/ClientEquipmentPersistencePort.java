package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida responsable de la persistencia de {@link ClientEquipment}.
 *
 * <p>Define las operaciones básicas que el adaptador de persistencia debe
 * implementar para consultar, almacenar y eliminar equipos de cliente.</p>
 */
public interface ClientEquipmentPersistencePort {

    /**
     * Busca un {@link ClientEquipment} por su identificador.
     *
     * @param clientEquipmentId identificador del {@link ClientEquipment} a consultar
     * @return {@link ClientEquipment} encontrado
     */
    Optional<ClientEquipment> findByID(UUID clientEquipmentId);

    /**
     * Obtiene todos los {@link ClientEquipment} registrados en el sistema.
     *
     * @return lista de {@link ClientEquipment}
     */
    List<ClientEquipment> findAll();

    /**
     * Crea o actualiza un {@link ClientEquipment} en la capa de persistencia.
     *
     * @param clientEquipment datos del {@link ClientEquipment} a persistir
     * @return {@link ClientEquipment} guardado
     */
    ClientEquipment save(ClientEquipment clientEquipment);

    /**
     * Elimina un {@link ClientEquipment} del sistema.
     *
     * @param clientEquipmentId identificador del {@link ClientEquipment} a eliminar
     */
    void delete(ClientEquipment clientEquipment);
}
