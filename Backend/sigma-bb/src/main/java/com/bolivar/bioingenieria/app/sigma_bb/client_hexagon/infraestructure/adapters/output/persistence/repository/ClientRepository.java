package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interfaz repositorio para la persistencia de entidades {@link ClientEntity}.
 *
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) estándar
 * para la gestión de clientes en la base de datos. Extiende JpaRepository
 * para aprovechar las funcionalidades que proporciona Spring Data JPA.
 */
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

        @Query(value = "SELECT p.k_cedula FROM representante_legal rl " +
                        "JOIN persona p ON rl.k_identificador = p.k_identificador " +
                        "WHERE rl.k_id_cliente = :clientId AND rl.b_estado_activo = true LIMIT 1", nativeQuery = true)
        String findRepresentanteLegalCedulaByClientId(@Param("clientId") String clientId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE representante_legal SET b_estado_activo = false WHERE k_id_cliente = :clientId", nativeQuery = true)
        void deactivateRepresentantesLegalesByClientId(@Param("clientId") String clientId);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO representante_legal (k_identificador, k_id_cliente, b_estado_activo) " +
                        "SELECT p.k_identificador, :clientId, true " +
                        "FROM persona p WHERE p.k_cedula = :cedula " +
                        "ON CONFLICT (k_identificador, k_id_cliente) DO UPDATE SET b_estado_activo = true", nativeQuery = true)
        void insertOrUpdateRepresentanteLegal(@Param("clientId") String clientId, @Param("cedula") String cedula);
}
