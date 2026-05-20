package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.CreateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.DeleteBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.UpdateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;

import java.util.List;

public interface BrandServicePort {
    List<Brand> findAll();
    Brand findById(String id);
    Brand save(CreateBrandCommand createBrandCommand);
    Brand update(String id, UpdateBrandCommand updateBrandCommand);
    void delete(DeleteBrandCommand deleteBrandCommand);
}
