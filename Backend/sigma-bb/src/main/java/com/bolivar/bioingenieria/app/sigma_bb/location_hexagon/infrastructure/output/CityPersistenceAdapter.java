package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output.CityPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities.CityEntity;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CityNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.mapper.CityPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.repository.SpringCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CityPersistenceAdapter implements CityPersistencePort {
    private final SpringCityRepository springCityRepository;
    private final CityPersistenceMapper cityPersistenceMapper;

    @Autowired
    public CityPersistenceAdapter(SpringCityRepository springCityRepository,
                                   CityPersistenceMapper cityPersistenceMapper) {
        this.springCityRepository = springCityRepository;
        this.cityPersistenceMapper = cityPersistenceMapper;
    }

    @Override
    public List<City> findAll() {
        List<CityEntity> entities = springCityRepository.findAll();
        return entities.stream()
                .map(cityPersistenceMapper::toCity).toList();
    }

    @Override
    public Optional<City> findById(String id) {
        return springCityRepository.findById(id)
                .map(cityPersistenceMapper::toCity);
    }

    @Override
    public City save(City city) {
        CityEntity entity = cityPersistenceMapper.toCityEntity(city);
        CityEntity saved = springCityRepository.save(entity);
        return cityPersistenceMapper.toCity(saved);
    }

    @Override
    public City update(String id, City city) {
        CityEntity existing = springCityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException(id));

        cityPersistenceMapper.updateEntityFromDomain(city, existing);

        CityEntity saved = springCityRepository.save(existing);

        return cityPersistenceMapper.toCity(saved);
    }

    @Override
    public void delete(String id) {
        if (!springCityRepository.existsById(id)) {
            throw new CityNotFoundException(id);
        }
        springCityRepository.deleteById(id);
    }
}
