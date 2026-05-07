package com.seguranca.sisincidentes.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estrutura padronizada para respostas de erro da API.
 *
 * <p>Todos os erros retornados pela API seguem este formato JSON,
 * garantindo consistência e facilidade de tratamento no cliente.</p>
 *
 * <pre>
 * {
 *   "status": 400,
 *   "mensagem": "Erro de validação",
 *   "timestamp": "2025-01-15T10:30:00",
 *   "erros": ["Campo 'codigo' é obrigatório."]
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /** Código HTTP do erro. */
    private int status;

    /** Mensagem descritiva do erro. */
    private String mensagem;

    /** Momento exato em que o erro ocorreu. */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Lista de erros detalhados (utilizada principalmente em erros de validação).
     * Pode ser {@code null} para erros simples.
     */
    private List<String> erros;
}
