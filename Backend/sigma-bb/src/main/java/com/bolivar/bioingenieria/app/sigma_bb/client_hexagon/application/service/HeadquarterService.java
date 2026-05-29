package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.HeadquarterServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.HeadquarterPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.HeadquarterFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de aplicación para la gestión de {@link Headquarter}.
 * Coordina las operaciones de negocio entre los puertos de entrada
 * ({@link HeadquarterServicePort}) y los adaptadores de persistencia
 * ({@link HeadquarterPersistencePort}).
 *
 * <p>Se encarga de validar, transformar y orquestar las operaciones CRUD sobre sedes
 * ({@link Headquarter}) y sus entidades relacionadas: áreas de servicio ({@link ServiceArea})
 * y gerentes ({@link Manager}).</p>
 */
@Service
@AllArgsConstructor
public class HeadquarterService implements HeadquarterServicePort {

    private final HeadquarterPersistencePort headquarterPersistencePort;
    //private final PersonComunicationPort personComunicationPort;

    /**
     * Busca una {@link Headquarter} por su identificador único.
     *
     * @param headquarterId identificador de la {@link Headquarter} a consultar
     * @return {@link Headquarter} encontrada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter findByID(UUID headquarterId) {
        //personComunicationPort.findById.map();
        return headquarterPersistencePort.findById(headquarterId)
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Obtiene todas las {@link Headquarter} registradas.
     *
     * @return lista de {@link Headquarter}
     */
    @Override
    public List<Headquarter> findAll() {
        return headquarterPersistencePort.findAll();
    }

    /**
     * Crea y almacena una nueva {@link Headquarter}.
     * Genera identificadores únicos para la sede, el gerente responsable
     * y todas las {@link ServiceArea} asociadas.
     *
     * @param headquarter datos de la {@link Headquarter} a persistir
     * @return {@link Headquarter} almacenada
     */
    @Override
    public Headquarter save(Headquarter headquarter) {

        headquarter.setIdentificadorSede(UUID.randomUUID());
        headquarter.setEstadoActivo(true);

        if (headquarter.getManagerList() != null) {
            for (Manager manager : headquarter.getManagerList()) {
                manager.setIdentificadorEncargado(UUID.randomUUID());
                manager.setEstadoActivo(true);
            }
        }

        if (headquarter.getServiceAreaList() != null) {
            for (ServiceArea serviceArea : headquarter.getServiceAreaList()) {
                serviceArea.setIdentificadorSede(headquarter.getIdentificadorSede());
                serviceArea.setIdentificadorAreaServicio(UUID.randomUUID());
                serviceArea.setEstadoActivo(true);
            }
        }

        return headquarterPersistencePort.save(headquarter);
    }

    /**
     * Actualiza la información de una {@link Headquarter} existente.
     *
     * @param headquarterId identificador de la {@link Headquarter} a actualizar
     * @param headquarter datos nuevos de la {@link Headquarter}
     * @return {@link Headquarter} actualizada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter update(UUID headquarterId, Headquarter headquarter) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.setNombreSede(headquarter.getNombreSede());
                    existingHeadquarter.setDireccionCalleSede(headquarter.getDireccionCalleSede());
                    existingHeadquarter.setDireccionCarreraSede(headquarter.getDireccionCarreraSede());
                    existingHeadquarter.setDireccionNumeroSede(headquarter.getDireccionNumeroSede());
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Elimina (marca como inactiva) una {@link Headquarter} del sistema.
     *
     * @param headquarterId identificador de la {@link Headquarter} a eliminar
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public void delete(UUID headquarterId) {
        headquarterPersistencePort.findById(headquarterId)
                .map(headquarter -> {
                    headquarter.setEstadoActivo(false);
                    return headquarterPersistencePort.save(headquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    // ------------------------------------------------------------
    // ----------- Operaciones CRUD para ServiceArea --------------
    // ------------------------------------------------------------

    /**
     * Agrega una {@link ServiceArea} a una {@link Headquarter}.
     * Genera un identificador único para la nueva área de servicio.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param serviceArea datos de la {@link ServiceArea} a agregar
     * @return {@link Headquarter} actualizada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter addServiceArea(UUID headquarterId, ServiceArea serviceArea) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    serviceArea.setIdentificadorSede(existingHeadquarter.getIdentificadorSede());
                    serviceArea.setIdentificadorAreaServicio(UUID.randomUUID());
                    existingHeadquarter.addServiceArea(serviceArea);
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Actualiza una {@link ServiceArea} asociada a una {@link Headquarter}.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param serviceAreaId identificador de la {@link ServiceArea} a actualizar
     * @param serviceArea datos nuevos de la {@link ServiceArea}
     * @return {@link Headquarter} actualizada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter updateServiceArea(UUID headquarterId, UUID serviceAreaId, ServiceArea serviceArea) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.getServiceAreaList().stream()
                            .filter(e -> e.getIdentificadorAreaServicio().equals(serviceAreaId))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setNombreAreaServicio(serviceArea.getNombreAreaServicio());
                            });
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Elimina (marca como inactiva) una {@link ServiceArea} asociada a una {@link Headquarter}.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param serviceAreaId identificador de la {@link ServiceArea} a eliminar
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public void deleteServiceArea(UUID headquarterId, UUID serviceAreaId) {
        headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.removeServiceArea(serviceAreaId);
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    // -----------------------------------------------------------
    // ------------- Operaciones CRUD para Manager ---------------
    // -----------------------------------------------------------

    /**
     * Agrega un {@link Manager} a una {@link Headquarter}.
     * Genera un identificador único para el nuevo gerente.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param manager datos del {@link Manager} a agregar
     * @return {@link Headquarter} actualizada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter addManger(UUID headquarterId, Manager manager) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.addManager(manager);
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Actualiza un {@link Manager} asociado a una {@link Headquarter}.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param managerId identificador del {@link Manager} a actualizar
     * @param manager datos nuevos del {@link Manager}
     * @return {@link Headquarter} actualizada
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter updateManger(UUID headquarterId, UUID managerId, Manager manager) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.getManagerList().stream()
                            .filter(e -> e.getIdentificadorEncargado().equals(managerId))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setTipoEncargado(manager.getTipoEncargado());
                            });
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }

    /**
     * Elimina (marca como inactivo) un {@link Manager} asociado a una {@link Headquarter}.
     *
     * @param headquarterId identificador de la {@link Headquarter}
     * @param managerId identificador del {@link Manager} a eliminar
     * @throws HeadquarterFoundException si la sede no existe
     */
    @Override
    public Headquarter deleteManger(UUID headquarterId, UUID managerId) {
        return headquarterPersistencePort.findById(headquarterId)
                .map(existingHeadquarter -> {
                    existingHeadquarter.removeManager(managerId);
                    return headquarterPersistencePort.save(existingHeadquarter);
                })
                .orElseThrow(HeadquarterFoundException::new);
    }
}
