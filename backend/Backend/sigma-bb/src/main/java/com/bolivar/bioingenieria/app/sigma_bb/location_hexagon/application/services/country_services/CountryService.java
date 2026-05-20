package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input.CountryServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output.CountryPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.CreateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.DeleteCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.UpdateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CountryNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService implements CountryServicePort {
    private final CountryPersistencePort countryPersistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public CountryService(CountryPersistencePort countryPersistencePort,
                          @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.countryPersistencePort = countryPersistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<Country> findAll() {
        return countryPersistencePort.findAll();
    }

    @Override
    public Country findById(String id) {
        return this.countryPersistencePort.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));
    }

    @Override
    public Country save(CreateCountryCommand command) {
        Country country = Country.create(command.id(), command.name());
        countryPersistencePort.save(country);
        dispatchEvents(country);
        return country;
    }

    @Override
    public Country update(String id, UpdateCountryCommand command) {
        Country country = countryPersistencePort.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));
        country.updateCountry(command.name());
        countryPersistencePort.update(id, country);
        dispatchEvents(country);
        return country;
    }

    @Override
    public void delete(DeleteCountryCommand command) {
        Country country = countryPersistencePort.findById(command.id())
                .orElseThrow(() -> new CountryNotFoundException(command.id()));
        country.deleteCountry();
        countryPersistencePort.delete(command.id());
        dispatchEvents(country);
    }

    private void dispatchEvents(Country aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
