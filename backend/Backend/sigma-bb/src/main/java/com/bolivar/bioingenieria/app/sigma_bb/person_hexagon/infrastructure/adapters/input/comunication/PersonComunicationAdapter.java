package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.comunication;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.PersonServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.PersonComunicationPort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.Respose.PersonComunicationResponse;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.comunication.mapper.PersonComunicationMapper;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PersonComunicationAdapter implements PersonComunicationPort {

    private final PersonServicePort personServicePort;
    private final PersonComunicationMapper personComunicationMapper;

    @Override
    public PersonComunicationResponse findById(UUID id) {
        return personComunicationMapper.toPersonComunicationResponse(personServicePort.findById(id));
    }
}
