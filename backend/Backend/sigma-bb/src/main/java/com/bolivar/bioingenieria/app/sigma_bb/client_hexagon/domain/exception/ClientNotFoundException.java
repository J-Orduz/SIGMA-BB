package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un cliente en el dominio.
 *
 * Esta excepción señala que una operación que esperaba localizar
 * un cliente por su identificador no obtuvo resultado y debe
 * ser manejada por la capa superior (por ejemplo, devolviendo
 * un 404 en una API REST).
 */
public class ClientNotFoundException extends RuntimeException {
}
