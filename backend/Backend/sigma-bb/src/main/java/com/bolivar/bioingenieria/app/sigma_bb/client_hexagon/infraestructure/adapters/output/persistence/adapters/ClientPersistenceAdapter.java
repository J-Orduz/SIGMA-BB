package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.adapters;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ClientPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper.ClientPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto de salida ClientPersistencePort.
 *
 * Proporciona las operaciones de persistencia para la entidad Client, actuando como
 * intermediario entre la lógica de aplicación y el repositorio de datos. Utiliza
 * ClientPersistenceMapper para las conversiones entre modelos de dominio y entidades
 * de persistencia.
 */
@Component
@RequiredArgsConstructor
public class ClientPersistenceAdapter implements ClientPersistencePort {

    private final ClientRepository clientRepository;
    private final ClientPersistenceMapper clientPersistenceMapper;

    /**
     * Busca un cliente por su identificador único.
     *
     * @param clientId Identificador único del cliente a buscar
     * @return Optional conteniendo el cliente si existe, Empty en caso contrario
     */
    @Override
    public Optional<Client> findById(String clientId) {
        return clientRepository.findById(clientId)
                .map(clientPersistenceMapper::toClient);
    }

    /**
    * Obtiene la lista completa de todos los clientes almacenados en la base de datos.
    *
    * @return Lista de todos los modelos de dominio Client
    */
    @Override
    public List<Client> findAll() {
        return clientPersistenceMapper.toClientList(clientRepository.findAll());
    }

    /**
     * Persiste un nuevo cliente o actualiza uno existente en la base de datos.
     *
     * @param client Modelo de dominio Client a persistir
     * @return Modelo de dominio Client con los datos persistidos
     */
    @Override
    public Client save(Client client) {
        return clientPersistenceMapper.toClient(
                clientRepository.save(
                        clientPersistenceMapper.toClientEntity(client)));
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param client Modelo de dominio Client a eliminar
     */
    @Override
    public void delete(Client client) {
        clientPersistenceMapper.toClient(
                clientRepository.save(
                        clientPersistenceMapper.toClientEntity(client)));
    }
}
