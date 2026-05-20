package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientEquipmentServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ClientEquipmentPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ClientEquipmentFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientEquipmentService implements ClientEquipmentServicePort
{
    private final ClientEquipmentPersistencePort clientEquipmentPersistencePort;

    @Override
    public ClientEquipment findByID(UUID clientEquipmentId) {
        return clientEquipmentPersistencePort.findByID(clientEquipmentId)
                .orElseThrow(ClientEquipmentFoundException::new);
    }

    @Override
    public List<ClientEquipment> findAll() {
        return clientEquipmentPersistencePort.findAll();
    }

    @Override
    public ClientEquipment save(ClientEquipment clientEquipment) {
        clientEquipment.setIdentificadorEquipoCliente(UUID.randomUUID());
        return clientEquipmentPersistencePort.save(clientEquipment);
    }

    @Override
    public ClientEquipment update(UUID clientEquipmentId, ClientEquipment clientEquipment) {
        return clientEquipmentPersistencePort.findByID(clientEquipmentId)
                .map(existingEquipment -> {
                    existingEquipment.setSerie(clientEquipment.getSerie());
                    existingEquipment.setFechaCompra(clientEquipment.getFechaCompra());
                    existingEquipment.setValorCompra(clientEquipment.getValorCompra());
                    existingEquipment.setNumeroInventario(clientEquipment.getNumeroInventario());
                    return clientEquipmentPersistencePort.save(existingEquipment);
                })
                .orElseThrow(ClientEquipmentFoundException::new);
    }

    @Override
    public void delete(UUID clientEquipmentId) {
        clientEquipmentPersistencePort.findByID(clientEquipmentId)
                .map(clientEquipment -> {
                    clientEquipment.setEstadoActivo(false);
                    return clientEquipmentPersistencePort.save(clientEquipment);
                }).orElseThrow(ClientEquipmentFoundException::new);

    }
}
