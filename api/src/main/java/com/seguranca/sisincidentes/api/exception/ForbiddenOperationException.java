package com.seguranca.sisincidentes.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário tenta realizar uma operação proibida
 * por regras de negócio ou restrição de perfil (RF04).
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenOperationException extends RuntimeException {
    
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
