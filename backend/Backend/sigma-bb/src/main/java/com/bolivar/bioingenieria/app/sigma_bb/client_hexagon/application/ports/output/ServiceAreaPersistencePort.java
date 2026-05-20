package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida responsable de la persistencia de {@link ServiceArea}.
 *
 * <p>Define las operaciones básicas que el adaptador de persistencia debe
 * implementar para consultar, almacenar y eliminar áreas de servicio.</p>
 */
public interface ServiceAreaPersistencePort {

    /**
     * Busca una {@link ServiceArea} por su identificador.
     *
     * @param id identificador de la {@link ServiceArea} a consultar (tipo {@link UUID})
     * @return {@link ServiceArea} encontrada o null si no existe
     */
    Optional<ServiceArea> findById(UUID id);

    /**
     * Obtiene todas las {@link ServiceArea} registradas en el sistema.
     *
     * @return lista de {@link ServiceArea}
     */
    List<ServiceArea> findAll();

    /**
     * Crea o actualiza una {@link ServiceArea} en la capa de persistencia.
     *
     * @param serviceArea datos de la {@link ServiceArea} a persistir
     * @return {@link ServiceArea} guardada
     */
    ServiceArea save(ServiceArea serviceArea);

    /**
     * Elimina una {@link ServiceArea} del sistema.
     *
     * @param serviceArea {@link ServiceArea} a eliminar
     */
    void delete(ServiceArea serviceArea);
}
