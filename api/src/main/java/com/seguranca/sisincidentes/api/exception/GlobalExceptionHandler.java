package com.seguranca.sisincidentes.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler global de exceções da API.
 *
 * <p>Centraliza o tratamento de todos os erros da aplicação,
 * garantindo respostas padronizadas via {@link ApiError} para qualquer
 * exceção lançada nos controllers.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata {@link ResourceNotFoundException}.
     * Retorna {@code 404 Not Found} quando um recurso não é localizado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());

        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata {@link MethodArgumentNotValidException} (erros de validação Bean Validation).
     * Retorna {@code 400 Bad Request} com a lista de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Erro de validação: {}", erros);

        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Erro de validação nos dados informados.")
                .erros(erros)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata {@link DataIntegrityViolationException} (violação de constraint no banco).
     * Retorna {@code 409 Conflict} quando, por exemplo, um código duplicado é inserido.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade de dados: {}", ex.getMostSpecificCause().getMessage());

        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .mensagem("Violação de integridade de dados. Verifique se o código já está em uso.")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Trata {@link IllegalArgumentException} para regras de negócio simples.
     * Retorna {@code 422 Unprocessable Entity}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());

        ApiError error = ApiError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .mensagem(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Fallback para qualquer exceção não tratada especificamente.
     * Retorna {@code 500 Internal Server Error}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        log.error("Erro interno não esperado: ", ex);

        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .mensagem("Erro interno no servidor. Tente novamente mais tarde.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
