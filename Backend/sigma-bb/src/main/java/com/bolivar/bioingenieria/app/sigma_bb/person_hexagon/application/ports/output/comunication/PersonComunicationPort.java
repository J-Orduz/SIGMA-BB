package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.comunication;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.comunication.respose.PersonComunicationResponse;

import java.util.UUID;

public interface PersonComunicationPort {

    PersonComunicationResponse findById(UUID id);


}
