package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.DashboardStatsDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para o módulo de Relatórios e Indicadores.
 *
 * <p><b>RF07 — Relatórios / RF09 — Estatísticas</b></p>
 *
 * <p>Expõe endpoints para o consumo de métricas consolidadas,
 * destinadas à construção de dashboards gerenciais.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios e Estatísticas", description = "RF07 / RF09 — Extração de métricas e indicadores de segurança")
public class RelatorioController {

    private final RelatorioService service;

    // =========================================================
    //  GET /api/relatorios/estatisticas — Obter Consolidado
    // =========================================================

    @Operation(
        summary = "Extrair Estatísticas do Dashboard",
        description = "Retorna um objeto consolidado contendo as agregações quantitativas de incidentes por status atual e severidade de vulnerabilidade."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Indicadores extraídos com sucesso",
            content = @Content(schema = @Schema(implementation = DashboardStatsDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno na consolidação dos dados",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/estatisticas")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        log.info("GET /api/relatorios/estatisticas — Solicitando consolidação de indicadores.");
        DashboardStatsDTO response = service.obterEstatisticasGerais();
        return ResponseEntity.ok(response);
    }
}