package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.EmailClient;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.PhoneClient;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada que define las operaciones de negocio disponibles
 * para la gestión de {@link Client}.
 *
 * <p>Esta interfaz establece el contrato que debe implementar el servicio
 * de aplicación para consultar, crear, actualizar y eliminar clientes,
 * así como administrar sus sedes, correos y teléfonos asociados.</p>
 */
public interface ClientServicePort {

    // ------------------------------------------------------------
    // ---------------- Operaciones CRUD para Client -------------
    // ------------------------------------------------------------

    /**
     * Busca un {@link Client} por su identificador único.
     *
     * @param clientId identificador del {@link Client} a consultar
     * @return {@link Client} encontrado
     */
    Client findByID(String clientId);

    /**
     * Obtiene todos los {@link Client} registrados.
     *
     * @return lista de {@link Client}
     */
    List<Client> findAll();

    /**
     * Crea y almacena un {@link Client}.
     *
     * @param client datos del {@link Client} a persistir
     * @return {@link Client} almacenado
     */
    Client save(Client client);

    /**
     * Actualiza la información de un {@link Client} existente.
     *
     * @param clientId identificador del {@link Client} a actualizar
     * @param client datos nuevos del {@link Client}
     * @return {@link Client} actualizado
     */
    Client update(String clientId, Client client);

    /**
     * Elimina (marca como inactivo) un {@link Client} del sistema.
     *
     * @param clientId identificador del {@link Client} a eliminar
     */
    void delete(String clientId);

    // ------------------------------------------------------------
    // ------------- Operaciones CRUD para EmailClient ------------
    // ------------------------------------------------------------

    /**
     * Agrega un correo electrónico a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param email correo a agregar como {@link EmailClient}
     * @return {@link Client} actualizado
     */
    Client addEmail(String clientId, EmailClient email);

    /**
     * Actualiza un correo electrónico asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param emailId identificador del {@link EmailClient} a actualizar
     * @param email datos nuevos del {@link EmailClient}
     * @return {@link Client} actualizado
     */
    Client updateEmail(String clientId, UUID emailId, EmailClient email);

    /**
     * Elimina un correo electrónico asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param emailId identificador del {@link EmailClient} a eliminar
     * @return {@link Client} actualizado
     */
    Client removeEmail(String clientId, UUID emailId);

    // ------------------------------------------------------------
    // ------------- Operaciones CRUD para PhoneClient ------------
    // ------------------------------------------------------------

    /**
     * Agrega un teléfono a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phone teléfono a agregar como {@link PhoneClient}
     * @return {@link Client} actualizado
     */
    Client addPhone(String clientId, PhoneClient phone);


    /**
     * Actualiza un teléfono asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phoneId identificador del {@link PhoneClient} a actualizar
     * @param phone datos nuevos del {@link PhoneClient}
     * @return {@link Client} actualizado
     */
    Client updatePhone(String clientId, UUID phoneId, PhoneClient phone);

    /**
     * Elimina un teléfono asociado a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param phoneId identificador del {@link PhoneClient} a eliminar
     * @return {@link Client} actualizado
     */
    Client removePhone(String clientId, UUID phoneId);

    // ------------------------------------------------------------
    // ------------- Operaciones CRUD para Headquarter ------------
    // ------------------------------------------------------------

    /**
     * Agrega una {@link Headquarter} a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarter datos de la {@link Headquarter} a agregar
     * @return {@link Client} actualizado
     */
    Client addHeadquarter(String clientId, Headquarter headquarter);


    /**
     * Actualiza una {@link Headquarter} asociada a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarterId identificador de la {@link Headquarter} a actualizar
     * @param headquarter datos nuevos de la {@link Headquarter}
     * @return {@link Client} actualizado
     */
    Client updateHeadquarter(String clientId, UUID headquarterId, Headquarter headquarter);

    /**
     * Elimina (marca como inactiva) una {@link Headquarter} asociada a un {@link Client}.
     *
     * @param clientId identificador del {@link Client}
     * @param headquarterId identificador de la {@link Headquarter} a eliminar
     * @return {@link Client} actualizado
     */
    Client removeHeadquarter(String clientId, UUID headquarterId);
}
