package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.BrandPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.BrandEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.BrandNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.BrandPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BrandPersistenceAdapter implements BrandPersistencePort {
    private final SpringBrandRepository springBrandRepository;
    private final BrandPersistenceMapper brandPersistenceMapper;

    @Autowired
    public BrandPersistenceAdapter(SpringBrandRepository springBrandRepository,
                                    BrandPersistenceMapper brandPersistenceMapper) {
        this.springBrandRepository = springBrandRepository;
        this.brandPersistenceMapper = brandPersistenceMapper;
    }

    @Override
    public List<Brand> findAll() {
        return springBrandRepository.findAll().stream()
                .map(brandPersistenceMapper::toBrand).toList();
    }

    @Override
    public Optional<Brand> findById(String id) {
        return springBrandRepository.findById(UUID.fromString(id))
                .map(brandPersistenceMapper::toBrand);
    }

    @Override
    public Brand save(Brand brand) {
        return brandPersistenceMapper.toBrand(
                springBrandRepository.save(brandPersistenceMapper.toBrandEntity(brand)));
    }

    @Override
    public Brand update(String id, Brand brand) {
        UUID uuid = UUID.fromString(id);
        BrandEntity existing = springBrandRepository.findById(uuid)
                .orElseThrow(() -> new BrandNotFoundException(id));
        brandPersistenceMapper.updateEntityFromDomain(brand, existing);
        return brandPersistenceMapper.toBrand(springBrandRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!springBrandRepository.existsById(uuid)) {
            throw new BrandNotFoundException(id);
        }
        springBrandRepository.deleteById(uuid);
    }
}
