package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ClientEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link Client}
 * y su representación de persistencia {@link ClientEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas. Integra además los
 * mapeadores de {@link EmailClientPersistenceMapper}, {@link PhoneClientPersistenceMapper}
 * y {@link HeadquarterPersistenceMapper} para manejar las relaciones de uno a muchos
 * con correos, teléfonos y sedes respectivamente.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                EmailClientPersistenceMapper.class,
                PhoneClientPersistenceMapper.class,
                HeadquarterPersistenceMapper.class})
public interface ClientPersistenceMapper {

    /**
     * Convierte un modelo de dominio Client a su representación de entidad ClientEntity.
     *
     * @param client Objeto de dominio con la información del cliente
     * @return Entidad ClientEntity lista para persistencia
     */
    ClientEntity toClientEntity(Client client);

    /**
     * Convierte una entidad de persistencia ClientEntity a su modelo de dominio Client.
     *
     * @param clientEntity Entidad de persistencia con la información del cliente
     * @return Modelo de dominio Client con los datos recuperados
     */
    Client toClient(ClientEntity clientEntity);

    /**
     * Convierte una lista de entidades de persistencia ClientEntity a una lista
     * de modelos de dominio Client.
     *
     * @param clientEntities Lista de entidades de persistencia con información de clientes
     * @return Lista de modelos de dominio Client
     */
    List<Client> toClientList(List<ClientEntity> clientEntities);

    /**
     * Vincula automáticamente las relaciones bidireccionales entre la entidad Client
     * y sus entidades relacionadas (EmailClientEntity, PhoneClientEntity y HeadquarterEntity)
     * después del mapeo inicial.
     *
     * Este método se ejecuta después del mapeo para asegurar que las referencias
     * inversas se establezcan correctamente en la base de datos.
     *
     * @param clientEntity Entidad ClientEntity con las relaciones a vincular
     */
    @AfterMapping
    default void linkChildren(@MappingTarget ClientEntity clientEntity) {
        if (clientEntity.getEmailClientList() != null) {
            clientEntity.getEmailClientList()
                    .forEach(email ->
                            email.setClient(clientEntity));
        }
        if (clientEntity.getPhoneClientList() != null) {
            clientEntity.getPhoneClientList()
                    .forEach(phone ->
                            phone.setClient(clientEntity));
        }
        if (clientEntity.getHeadquarterList() != null) {
            clientEntity.getHeadquarterList()
                    .forEach(headquarter ->
                            headquarter.setClient(clientEntity));
        }
    }
}
