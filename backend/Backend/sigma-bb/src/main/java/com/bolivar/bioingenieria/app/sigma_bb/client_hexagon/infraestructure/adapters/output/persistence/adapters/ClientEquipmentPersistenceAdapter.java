package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.adapters;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ClientEquipmentPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper.ClientEquipmentPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository.ClientEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia que implementa el puerto de salida {@link ClientEquipmentPersistencePort}.
 *
 * Proporciona las operaciones de persistencia para la entidad ClientEquipment, actuando como
 * intermediario entre la lógica de aplicación y el repositorio de datos. Utiliza
 * ClientEquipmentPersistenceMapper para las conversiones entre modelos de dominio y entidades
 * de persistencia.
 */
@Component
@RequiredArgsConstructor
public class ClientEquipmentPersistenceAdapter implements ClientEquipmentPersistencePort {

    private final ClientEquipmentRepository clientEquipmentRepository;
    private final ClientEquipmentPersistenceMapper clientEquipmentPersistenceMapper;

    /**
     * Busca un equipo de cliente por su identificador único.
     *
     * @param clientEquipmentId Identificador único (UUID) del equipo de cliente a buscar
     * @return Optional conteniendo el equipo de cliente si existe, Empty en caso contrario
     */
    @Override
    public Optional<ClientEquipment> findByID(UUID clientEquipmentId) {
        return clientEquipmentRepository.findById(clientEquipmentId)
                .map(clientEquipmentPersistenceMapper::toClientEquipment);
    }

    /**
    * Obtiene la lista completa de todos los equipos de cliente almacenados en la base de datos.
    *
    * @return Lista de todos los modelos de dominio ClientEquipment
    */
    @Override
    public List<ClientEquipment> findAll() {
        return clientEquipmentPersistenceMapper.toClientEquipmentList(
                clientEquipmentRepository.findAll());
    }

    /**
     * Persiste un nuevo equipo de cliente o actualiza uno existente en la base de datos.
     *
     * @param clientEquipment Modelo de dominio ClientEquipment a persistir
     * @return Modelo de dominio ClientEquipment con los datos persistidos
     */
    @Override
    public ClientEquipment save(ClientEquipment clientEquipment) {
        return clientEquipmentPersistenceMapper.toClientEquipment(
                clientEquipmentRepository.save(
                        clientEquipmentPersistenceMapper.toClientEquipmentEntity(clientEquipment)));
    }

    /**
     * Elimina un equipo de cliente del sistema.
     *
     * @param clientEquipmentId Identificador del equipo de cliente a eliminar
     */
    @Override
    public void delete(ClientEquipment clientEquipmentId) {
        clientEquipmentPersistenceMapper.toClientEquipment(
                clientEquipmentRepository.save(
                        clientEquipmentPersistenceMapper.toClientEquipmentEntity(clientEquipmentId)));

    }
}
