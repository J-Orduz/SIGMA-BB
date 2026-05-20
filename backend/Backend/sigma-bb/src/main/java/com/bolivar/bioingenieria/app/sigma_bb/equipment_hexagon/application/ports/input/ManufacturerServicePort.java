package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.CreateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.DeleteManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.UpdateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;

import java.util.List;

public interface ManufacturerServicePort {
    List<Manufacturer> findAll();
    Manufacturer findById(String id);
    Manufacturer save(CreateManufacturerCommand createManufacturerCommand);
    Manufacturer update(String id, UpdateManufacturerCommand updateManufacturerCommand);
    void delete(DeleteManufacturerCommand deleteManufacturerCommand);
}
