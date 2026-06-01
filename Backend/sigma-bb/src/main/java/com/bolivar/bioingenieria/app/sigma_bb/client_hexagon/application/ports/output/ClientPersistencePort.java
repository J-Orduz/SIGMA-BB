package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida que define las operaciones de persistencia para {@link Client}.
 * Esta interfaz establece el contrato que debe implementar el adaptador
 * de persistencia para acceder a la base de datos y gestionar
 * la persistencia de clientes.
 */
public interface ClientPersistencePort {

    /**
     * Busca un {@link Client} por su identificador único.
     *
     * @param clientId identificador del {@link Client} a consultar
     * @return {@link Optional} con el {@link Client} encontrado, vacío si no existe
     */
    Optional<Client> findById(String clientId);

    /**
     * Obtiene todos los {@link Client} registrados en la base de datos.
     *
     * @return lista de {@link Client}
     */
    List<Client> findAll();

    /**
     * Persiste un {@link Client} en la base de datos.
     * Si el {@link Client} es nuevo, lo crea; si ya existe, lo actualiza.
     *
     * @param client {@link Client} a persistir
     * @return {@link Client} persistido
     */
    Client save(Client client);

    /**
     * Elimina un {@link Client} de la base de datos.
     *
     * @param client {@link Client} a eliminar
     */
    void delete(Client client);

    /**
     * Sincroniza la relación del representante legal en la base de datos para el cliente dado.
     *
     * @param clientId identificador del cliente
     * @param cedula cédula del representante legal a vincular
     */
    void saveRepresentanteLegal(String clientId, String cedula);
}
