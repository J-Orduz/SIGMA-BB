package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.exception;

/**
 * Excepción lanzada cuando se intenta acceder a una persona que no existe en el sistema.
 * Esta excepción se dispara en operaciones de búsqueda o modificación cuando el identificador
 * de la persona no se encuentran en la base de datos.
 */
public class PersonNotFoundException extends RuntimeException{
}
