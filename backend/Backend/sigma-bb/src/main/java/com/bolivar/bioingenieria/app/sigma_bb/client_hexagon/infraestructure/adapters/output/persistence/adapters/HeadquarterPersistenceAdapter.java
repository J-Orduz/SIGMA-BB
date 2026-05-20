package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.adapters;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.HeadquarterPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper.HeadquarterPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository.HeadquarterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia que implementa el puerto de salida {@link HeadquarterPersistencePort}.
 *
 * Proporciona las operaciones de persistencia para la entidad Headquarter, actuando como
 * intermediario entre la lógica de aplicación y el repositorio de datos. Utiliza
 * HeadquarterPersistenceMapper para las conversiones entre modelos de dominio y entidades
 * de persistencia.
 */
@Component
@RequiredArgsConstructor
public class HeadquarterPersistenceAdapter implements HeadquarterPersistencePort {

    private final HeadquarterRepository headquarterRepository;
    private final HeadquarterPersistenceMapper headquarterPersistenceMapper;

    /**
     * Busca una sede por su identificador único.
     *
     * @param headquarterId Identificador único (UUID) de la sede a buscar
     * @return Optional conteniendo la sede si existe, Empty en caso contrario
     */
    @Override
    public Optional<Headquarter> findById(UUID headquarterId) {
        return headquarterRepository.findById(headquarterId)
                .map(headquarterPersistenceMapper::toHeadquarter);
    }

    /**
     * Obtiene la lista completa de todas las sedes almacenadas en la base de datos.
     *
     * @return Lista de todos los modelos de dominio Headquarter
     */
    @Override
    public List<Headquarter> findAll() {
        return headquarterPersistenceMapper.toHeadquarterList(
                headquarterRepository.findAll());
    }

    /**
     * Persiste una nueva sede o actualiza una existente en la base de datos.
     *
     * @param headquarter Modelo de dominio Headquarter a persistir
     * @return Modelo de dominio Headquarter con los datos persistidos
     */
    @Override
    public Headquarter save(Headquarter headquarter) {
        return headquarterPersistenceMapper.toHeadquarter(
                headquarterRepository.save(
                        headquarterPersistenceMapper.toHeadquarterEntity(headquarter)));
    }

    /**
     * Elimina una sede del sistema.
     *
     * @param headquarter Modelo de dominio Headquarter a eliminar
     */
    @Override
    public void delete(Headquarter headquarter) {
        headquarterPersistenceMapper.toHeadquarter(
                headquarterRepository.save(
                        headquarterPersistenceMapper.toHeadquarterEntity(headquarter)));
    }
}
