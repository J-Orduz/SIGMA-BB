package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de {@link Headquarter}.
 *
 * <p>Define las operaciones básicas necesarias para consultar, almacenar
 * y eliminar sedes dentro de la capa de aplicación.</p>
 */
public interface HeadquarterPersistencePort {

    /**
     * Busca una {@link Headquarter} por su identificador único.
     *
     * @param headquarterId identificador de la {@link Headquarter} a consultar
     * @return {@link Headquarter} encontrada
     */
    Optional<Headquarter> findById(UUID headquarterId);

    /**
     * Obtiene todas las {@link Headquarter} registradas.
     *
     * @return lista de {@link Headquarter}
     */
    List<Headquarter> findAll();

    /**
     * Crea o actualiza una {@link Headquarter} en el sistema.
     *
     * @param headquarter datos de la {@link Headquarter} a persistir
     * @return {@link Headquarter} guardada
     */
    Headquarter save(Headquarter headquarter);

    /**
     * Elimina una {@link Headquarter} del sistema.
     *
     * @param headquarter {@link Headquarter} a eliminar
     */
    void delete(Headquarter headquarter);

}
