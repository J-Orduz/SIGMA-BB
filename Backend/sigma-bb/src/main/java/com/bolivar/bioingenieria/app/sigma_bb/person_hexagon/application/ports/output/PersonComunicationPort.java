package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.Respose.PersonComunicationResponse;

import java.util.UUID;

public interface PersonComunicationPort {

    PersonComunicationResponse findById(UUID id);


}
