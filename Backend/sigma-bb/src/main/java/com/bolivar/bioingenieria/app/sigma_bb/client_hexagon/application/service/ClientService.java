package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ClientPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ClientNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de aplicación para la gestión de {@link Client}.
 * Coordina las operaciones de negocio entre los puertos de entrada
 * ({@link ClientServicePort}) y los adaptadores de persistencia
 * ({@link ClientPersistencePort}).
 *
 * <p>Se encarga de validar, transformar y orquestar las operaciones CRUD sobre clientes
 * y sus contactos asociados (correos y teléfonos), así como la gestión de sedes
 * ({@link Headquarter}) asociadas a un cliente.</p>
 */
@Service
@AllArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientPersistencePort clientPersistencePort;

    // ---------------------------------------------------------------
    // -------------------- METODOS DE CLIENTE -----------------------
    // ---------------------------------------------------------------

    /**
     * Busca un {@link Client} por su identificador único.
     *
     * @param clientId identificador del {@link Client} a consultar
     * @return {@link Client} encontrado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client findByID(String clientId) {
        return clientPersistencePort.findById(clientId)
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Obtiene todos los {@link Client} registrados.
     *
     * @return lista de {@link Client}
     */
    @Override
    public List<Client> findAll() {
        return clientPersistencePort.findAll();
    }

    /**
     * Crea y almacena un nuevo {@link Client}.
     * Genera identificadores únicos para los {@link EmailClient} y {@link PhoneClient} asociados.
     *
     * @param client {@link Client} a persistir
     * @return {@link Client} almacenado
     */
    @Override
    public Client save(Client client) {
        // Asegurar que el nuevo cliente se registre como ACTIVO
        client.setEstadoActivo(true);

        // Guarda de null: si el mapper no recibió la lista, se usa una lista vacía
        // para evitar NullPointerException cuando el request no incluye contactos o sedes.
        if (client.getEmailClientList() == null) {
            client.setEmailClientList(java.util.Collections.emptyList());
        }
        if (client.getPhoneClientList() == null) {
            client.setPhoneClientList(java.util.Collections.emptyList());
        }
        if (client.getHeadquarterList() == null) {
            client.setHeadquarterList(java.util.Collections.emptyList());
        }

        for(EmailClient email : client.getEmailClientList()){
            email.setIdentificadorCorreoCliente(UUID.randomUUID());
            email.setEstadoActivo(true); // Asegurar que el correo esté ACTIVO por defecto
        }
        for(PhoneClient phone : client.getPhoneClientList()){
            phone.setIdentificadorTelefonoCliente(UUID.randomUUID());
            phone.setEstadoActivo(true); // Asegurar que el teléfono esté ACTIVO por defecto
        }
        for(Headquarter headquarter : client.getHeadquarterList()){
            headquarter.setIdentificadorSede(UUID.randomUUID());
            if (headquarter.getManagerList() == null) {
                headquarter.setManagerList(java.util.Collections.emptyList());
            }
            if (headquarter.getServiceAreaList() == null) {
                headquarter.setServiceAreaList(java.util.Collections.emptyList());
            }
            for (Manager manager : headquarter.getManagerList()) {
                manager.setIdentificadorEncargado(UUID.randomUUID());
            }
            for (ServiceArea serviceArea : headquarter.getServiceAreaList()) {
                serviceArea.setIdentificadorAreaServicio(UUID.randomUUID());
                if (serviceArea.getManagerList() == null) {
                    serviceArea.setManagerList(java.util.Collections.emptyList());
                }
                if (serviceArea.getClientEquipmentList() == null) {
                    serviceArea.setClientEquipmentList(java.util.Collections.emptyList());
                }
                for (Manager manager : serviceArea.getManagerList()) {
                    manager.setIdentificadorEncargado(UUID.randomUUID());
                }
                for (ClientEquipment clientEquipment : serviceArea.getClientEquipmentList()) {
                    clientEquipment.setIdentificadorEquipoCliente(UUID.randomUUID());
                }
            }
        }
        Client savedClient = clientPersistencePort.save(client);
        if (client.getIdentificadorRepresentante() != null) {
            clientPersistencePort.saveRepresentanteLegal(savedClient.getIdentificadorCliente(), client.getIdentificadorRepresentante());
        }
        savedClient.setIdentificadorRepresentante(client.getIdentificadorRepresentante());
        return savedClient;
    }

    /**
     * Actualiza la información de un {@link Client} existente.
     *
     * @param clientId identificador del {@link Client} a actualizar
     * @param client datos nuevos del {@link Client}
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client update(String clientId, Client client) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.setTipoIdentifiacion(client.getTipoIdentifiacion());
                    existingClient.setRazonSocial(client.getRazonSocial());
                    existingClient.setIdentificadorPais(client.getIdentificadorPais());
                    
                    // Actualizar correos de manera segura para Hibernate
                    if (client.getEmailClientList() != null) {
                        for (EmailClient email : client.getEmailClientList()) {
                            if (email.getIdentificadorCorreoCliente() == null) {
                                email.setIdentificadorCorreoCliente(UUID.randomUUID());
                            }
                            email.setEstadoActivo(true);
                        }
                        existingClient.getEmailClientList().clear();
                        existingClient.getEmailClientList().addAll(client.getEmailClientList());
                    }
                    
                    // Actualizar teléfonos de manera segura para Hibernate
                    if (client.getPhoneClientList() != null) {
                        for (PhoneClient phone : client.getPhoneClientList()) {
                            if (phone.getIdentificadorTelefonoCliente() == null) {
                                phone.setIdentificadorTelefonoCliente(UUID.randomUUID());
                            }
                            phone.setEstadoActivo(true);
                        }
                        existingClient.getPhoneClientList().clear();
                        existingClient.getPhoneClientList().addAll(client.getPhoneClientList());
                    }
                    
                    Client savedClient = clientPersistencePort.save(existingClient);
                    if (client.getIdentificadorRepresentante() != null) {
                        clientPersistencePort.saveRepresentanteLegal(clientId, client.getIdentificadorRepresentante());
                    }
                    savedClient.setIdentificadorRepresentante(client.getIdentificadorRepresentante());
                    return savedClient;
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Elimina un {@link Client} del sistema cambiando su estado a inactivo.
     *
     * @param clientId identificador del {@link Client} a eliminar
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public void delete(String clientId) {
        clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.setEstadoActivo(false);
                    
                    if (existingClient.getEmailClientList() != null) {
                        for (EmailClient email : existingClient.getEmailClientList()) {
                            email.setEstadoActivo(false);
                        }
                    }
                    
                    if (existingClient.getPhoneClientList() != null) {
                        for (PhoneClient phone : existingClient.getPhoneClientList()) {
                            phone.setEstadoActivo(false);
                        }
                    }
                    
                    if (existingClient.getHeadquarterList() != null) {
                        for (Headquarter hq : existingClient.getHeadquarterList()) {
                            hq.setEstadoActivo(false);
                            if (hq.getServiceAreaList() != null) {
                                for (ServiceArea area : hq.getServiceAreaList()) {
                                    area.setEstadoActivo(false);
                                }
                            }
                        }
                    }
                    
                    Client savedClient = clientPersistencePort.save(existingClient);
                    clientPersistencePort.saveRepresentanteLegal(clientId, null);
                    return savedClient;
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // ----------------------- Métodos CRUD de EmailClient --------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Agrega un correo electrónico a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param email {@link EmailClient} a agregar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client addEmail(String clientId, EmailClient email) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    email.setIdentificadorCorreoCliente(UUID.randomUUID());
                    existingClient.addEmail(email);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Actualiza un correo electrónico asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param emailId identificador del {@link EmailClient} a actualizar
     * @param email datos nuevos del {@link EmailClient}
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client updateEmail(String clientId, UUID emailId, EmailClient email) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.getEmailClientList().stream()
                            .filter(e -> e.getIdentificadorCorreoCliente().equals(emailId))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setCorreoCliente(email.getCorreoCliente());
                            });
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Elimina un correo electrónico asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param emailId identificador del {@link EmailClient} a eliminar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client removeEmail(String clientId, UUID emailId) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.removeEmail(emailId);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // ----------------------- Métodos CRUD de PhoneClient ---------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Agrega un teléfono a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phone {@link PhoneClient} a agregar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client addPhone(String clientId, PhoneClient phone) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    phone.setIdentificadorTelefonoCliente(UUID.randomUUID());
                    existingClient.addPhone(phone);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Actualiza un teléfono asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phoneId identificador del {@link PhoneClient} a actualizar
     * @param phone datos nuevos del {@link PhoneClient}
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client updatePhone(String clientId, UUID phoneId, PhoneClient phone) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.getPhoneClientList().stream()
                            .filter(p -> p.getIdentificadorTelefonoCliente().equals(phoneId))
                            .findFirst()
                            .ifPresent(p -> {
                                p.setTelefonoCliente(phone.getTelefonoCliente());
                            });
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Elimina un teléfono asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phoneId identificador del {@link PhoneClient} a eliminar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client removePhone(String clientId, UUID phoneId) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.removePhone(phoneId);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // ----------------------- Métodos CRUD de Headquarter ---------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Agrega una {@link Headquarter} a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarter {@link Headquarter} a agregar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client addHeadquarter(String clientId, Headquarter headquarter) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.addHeadquarter(headquarter);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Actualiza una {@link Headquarter} asociada a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarterId identificador de la {@link Headquarter} a actualizar
     * @param headquarter datos nuevos de la {@link Headquarter}
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client updateHeadquarter(String clientId, UUID headquarterId, Headquarter headquarter) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.getHeadquarterList().stream()
                            .filter(h -> h.getIdentificadorSede().equals(headquarterId))
                            .findFirst()
                            .ifPresent(h -> {
                                h.setNombreSede(headquarter.getNombreSede());
                                h.setDireccionCalleSede(headquarter.getDireccionCalleSede());
                                h.setDireccionCarreraSede(headquarter.getDireccionCarreraSede());
                                h.setDireccionNumeroSede(headquarter.getDireccionNumeroSede());
                            });
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Elimina (marca como inactiva) una {@link Headquarter} asociada a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarterId identificador de la {@link Headquarter} a eliminar
     * @return {@link Client} actualizado
     * @throws ClientNotFoundException si el cliente no existe
     */
    @Override
    public Client removeHeadquarter(String clientId, UUID headquarterId) {
        return clientPersistencePort.findById(clientId)
                .map(existingClient -> {
                    existingClient.removeHeadquarter(headquarterId);
                    return clientPersistencePort.save(existingClient);
                })
                .orElseThrow(ClientNotFoundException::new);
    }
}
