package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input.CityServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output.CityPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.CityPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.CreateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.DeleteCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.UpdateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CityNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements CityServicePort {
    private final CityPersistencePort cityPersistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public CityService(CityPersistencePort cityPersistencePort,
                       @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.cityPersistencePort = cityPersistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<City> findAll() {
        return cityPersistencePort.findAll();
    }

    @Override
    public City findById(String id) {
        return this.cityPersistencePort.findById(id)
                .orElseThrow(() -> new CityNotFoundException(id));
    }

    @Override
    public City save(CreateCityCommand command) {
        City city = City.create(command.id(), command.name(), command.countryId());
        cityPersistencePort.save(city);
        dispatchEvents(city);
        return city;
    }

    @Override
    public City update(String id, UpdateCityCommand command) {
        City city = cityPersistencePort.findById(id).orElseThrow(() -> new CityNotFoundException(id));
        city.updateCity(command.id(), command.name(), command.countryId());
        cityPersistencePort.update(id, city);
        dispatchEvents(city);
        return city;
    }

    @Override
    public void delete(DeleteCityCommand deleteCityCommand) {
        City city = cityPersistencePort.findById(deleteCityCommand.id())
                .orElseThrow(() -> new CityNotFoundException(deleteCityCommand.id()));
        city.deleteCity();
        cityPersistencePort.delete(deleteCityCommand.id());
        dispatchEvents(city);
    }

    @Override
    public City patchUpdate(String id, CityPatchCommand command) {
        City city = cityPersistencePort.findById(id)
                .orElseThrow(() -> new CityNotFoundException(id));
        city.updateCityPatch(command.name(), command.countryId());
        cityPersistencePort.update(id, city);
        dispatchEvents(city);
        return city;
    }

    private void dispatchEvents(City aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
