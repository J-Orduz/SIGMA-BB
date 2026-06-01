package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.BrandServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.BrandPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.BrandPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.CreateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.DeleteBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.UpdateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements BrandServicePort {
    private final BrandPersistencePort brandPersistencePort;

    @Autowired
    public BrandService(BrandPersistencePort brandPersistencePort) {
        this.brandPersistencePort = brandPersistencePort;
    }

    @Override
    public List<Brand> findAll() {
        return brandPersistencePort.findAll();
    }

    @Override
    public Brand findById(String id) {
        return this.brandPersistencePort.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }

    @Override
    public Brand save(CreateBrandCommand command) {
        Brand brand = Brand.create(command.name());
        brandPersistencePort.save(brand);
        return brand;
    }

    @Override
    public Brand update(String id, UpdateBrandCommand command) {
        Brand brand = brandPersistencePort.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
        brand.updateBrand(command.name());
        brandPersistencePort.update(id, brand);
        return brand;
    }

    @Override
    public void delete(DeleteBrandCommand command) {
        Brand brand = brandPersistencePort.findById(command.id())
                .orElseThrow(() -> new BrandNotFoundException(command.id()));
        brand.deleteBrand();
        brandPersistencePort.delete(command.id());
    }

    @Override
    public Brand patchUpdate(String id, BrandPatchCommand command) {
        Brand brand = brandPersistencePort.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
        brand.updateBrandPatch(command.name());
        brandPersistencePort.update(id, brand);
        return brand;
    }
}
