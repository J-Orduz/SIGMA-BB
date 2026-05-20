package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ServiceAreaServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ServiceAreaPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ServiceAreaFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de aplicación para la gestión de {@link ServiceArea}.
 * Coordina las operaciones de negocio entre los puertos de entrada
 * ({@link ServiceAreaServicePort}) y los adaptadores de persistencia
 * ({@link ServiceAreaPersistencePort}).
 *
 * <p>Se encarga de validar, transformar y orquestar las operaciones CRUD sobre áreas
 * de servicio y sus entidades relacionadas: equipos cliente ({@link ClientEquipment})
 * y gerentes ({@link Manager}).</p>
 */
@Service
@AllArgsConstructor
public class ServiceAreaService implements ServiceAreaServicePort {

    private final ServiceAreaPersistencePort serviceAreaPersistencePort;

    /**
     * Busca una {@link ServiceArea} por su identificador único.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea} a consultar
     * @return {@link ServiceArea} encontrada
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public ServiceArea findById(UUID serviceAreaId) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Obtiene todas las {@link ServiceArea} registradas.
     *
     * @return lista de {@link ServiceArea}
     */
    @Override
    public List<ServiceArea> findAll() {
        return serviceAreaPersistencePort.findAll();
    }

    /**
     * Crea y almacena una nueva {@link ServiceArea}.
     * Genera identificadores únicos para el área y todos los {@link ClientEquipment} asociados.
     *
     * @param serviceArea datos de la {@link ServiceArea} a persistir
     * @return {@link ServiceArea} almacenada
     */
    @Override
    public ServiceArea save(ServiceArea serviceArea) {

        serviceArea.setIdentificadorAreaServicio(UUID.randomUUID());

        for (ClientEquipment equipment : serviceArea.getClientEquipmentList()) {
            equipment.setIdentificadorAreaServicio(serviceArea.getIdentificadorAreaServicio());
            equipment.setIdentificadorEquipoCliente(UUID.randomUUID());
        }

        return serviceAreaPersistencePort.save(serviceArea);
    }

    /**
     * Actualiza la información de una {@link ServiceArea} existente.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea} a actualizar
     * @param serviceArea datos nuevos de la {@link ServiceArea}
     * @return {@link ServiceArea} actualizada
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public ServiceArea update(UUID serviceAreaId, ServiceArea serviceArea) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.setNombreAreaServicio(serviceArea.getNombreAreaServicio());
                    return serviceAreaPersistencePort.save(existingServiceArea);
                })
                .orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Elimina (marca como inactiva) una {@link ServiceArea} del sistema.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea} a eliminar
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public void delete(UUID serviceAreaId) {
        serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.setEstadoActivo(false);
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);

    }

    // ------------------------------------------------------------
    // -------------------CRUD CLIENT EQUIPMENT--------------------
    // ------------------------------------------------------------

    /**
     * Agrega un {@link ClientEquipment} a una {@link ServiceArea}.
     * Genera un identificador único para el nuevo equipo.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param clientEquipment datos del {@link ClientEquipment} a agregar
     * @return {@link ServiceArea} actualizada
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public ServiceArea addClientEquipment(UUID serviceAreaId, ClientEquipment clientEquipment) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    clientEquipment.setIdentificadorAreaServicio(existingServiceArea.getIdentificadorAreaServicio());
                    clientEquipment.setIdentificadorEquipoCliente(UUID.randomUUID());
                    existingServiceArea.addServiceArea(clientEquipment);
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Actualiza un {@link ClientEquipment} existente dentro de una {@link ServiceArea}.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param clientEquipmentId identificador del {@link ClientEquipment} a actualizar
     * @param clientEquipment datos nuevos del {@link ClientEquipment}
     * @return {@link ServiceArea} actualizada
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public ServiceArea updateClientEquipment(UUID serviceAreaId, UUID clientEquipmentId, ClientEquipment clientEquipment) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.getClientEquipmentList().stream()
                            .filter(e -> e.getIdentificadorEquipoCliente().equals(clientEquipmentId))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setSerie(clientEquipment.getSerie());
                                e.setFechaCompra(clientEquipment.getFechaCompra());
                                e.setValorCompra(clientEquipment.getValorCompra());
                                e.setNumeroInventario(clientEquipment.getNumeroInventario());
                            });
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Elimina (marca como inactivo) un {@link ClientEquipment} asociado a una {@link ServiceArea}.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param clientEquipmentId identificador del {@link ClientEquipment} a eliminar
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public void deleteClientEquipment(UUID serviceAreaId, UUID clientEquipmentId) {
        serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.getClientEquipmentList().stream()
                            .filter(e -> e.getIdentificadorEquipoCliente().equals(clientEquipmentId))
                            .findFirst()
                            .ifPresent(e -> e.setEstadoActivo(false));
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }

    // ------------------------------------------------------------
    // -----------------------CRUD MANAGER-------------------------
    // ------------------------------------------------------------

    /**
     * Agrega un {@link Manager} a una {@link ServiceArea}.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param manager datos del {@link Manager} a agregar
     * @return {@link ServiceArea} actualizada
     * @throws ServiceAreaFoundException si el área no existe
     */
    @Override
    public ServiceArea addManger(UUID serviceAreaId, Manager manager) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.addManager(manager);
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Actualiza un {@link Manager} asociado a una {@link ServiceArea}.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param managerId identificador del {@link Manager} a actualizar
     * @param manager datos nuevos del {@link Manager}
     * @return {@link ServiceArea} actualizada
     * @throws ServiceAreaFoundException si el área no existe o el gerente no coincide
     */
    @Override
    public ServiceArea updateManger(UUID serviceAreaId, UUID managerId, Manager manager) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.getManagerList().stream()
                            .filter(m -> m.getIdentificadorEncargado().equals(managerId))
                            .findFirst()
                            .ifPresent(m -> {
                                m.setTipoEncargado(manager.getTipoEncargado());
                            });
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }

    /**
     * Elimina (marca como inactivo) un {@link Manager} asociado a una {@link ServiceArea}.
     *
     * @param serviceAreaId identificador de la {@link ServiceArea}
     * @param managerId identificador del {@link Manager} a eliminar
     * @throws ServiceAreaFoundException si el área no existe o el gerente no coincide
     */
    @Override
    public ServiceArea deleteManger(UUID serviceAreaId, UUID managerId) {
        return serviceAreaPersistencePort.findById(serviceAreaId)
                .map(existingServiceArea -> {
                    existingServiceArea.removeManager(managerId);
                    return serviceAreaPersistencePort.save(existingServiceArea);
                }).orElseThrow(ServiceAreaFoundException::new);
    }
}
