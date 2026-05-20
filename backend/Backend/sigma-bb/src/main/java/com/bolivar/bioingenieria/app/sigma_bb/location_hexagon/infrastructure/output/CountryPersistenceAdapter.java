package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output.CountryPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities.CountryEntity;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CountryNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.mapper.CountryPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.repository.SpringCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CountryPersistenceAdapter implements CountryPersistencePort {
    private final SpringCountryRepository springCountryRepository;
    private final CountryPersistenceMapper countryPersistenceMapper;

    @Autowired
    public CountryPersistenceAdapter(SpringCountryRepository springCountryRepository,
                                     CountryPersistenceMapper countryPersistenceMapper) {
        this.springCountryRepository = springCountryRepository;
        this.countryPersistenceMapper = countryPersistenceMapper;
    }

    @Override
    public List<Country> findAll() {
        List<CountryEntity> entities = springCountryRepository.findAll();
        return entities.stream()
                .map(countryPersistenceMapper::toCountry).toList();
    }

    @Override
    public Optional<Country> findById(String id) {
        return springCountryRepository.findById(id)
                .map(countryPersistenceMapper::toCountry);
    }

    @Override
    public Country save(Country country) {
        CountryEntity entity = countryPersistenceMapper.toCountryEntity(country);
        CountryEntity saved = springCountryRepository.save(entity);
        return countryPersistenceMapper.toCountry(saved);
    }

    @Override
    public Country update(String id, Country country) {
        CountryEntity existing = springCountryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        countryPersistenceMapper.updateEntityFromDomain(country, existing);

        CountryEntity saved = springCountryRepository.save(existing);

        return countryPersistenceMapper.toCountry(saved);
    }

    @Override
    public void delete(String id) {
        if (!springCountryRepository.existsById(id)) {
            throw new CountryNotFoundException(id);
        }
        springCountryRepository.deleteById(id);
    }
}
