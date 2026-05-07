package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO de saída para consolidação de indicadores de segurança.
 *
 * <p><b>RF07 — Relatórios / RF09 — Estatísticas</b></p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DashboardStats", description = "Consolidado de estatísticas de incidentes para renderização de relatórios e dashboards")
public class DashboardStatsDTO {

    @Schema(description = "Mapeamento quantitativo de incidentes agrupados pelo status atual", example = "{\"ABERTO\": 5, \"RESOLVIDO\": 12}")
    private Map<String, Long> totalPorStatus;

    @Schema(description = "Mapeamento quantitativo de incidentes agrupados pela severidade das vulnerabilidades associadas", example = "{\"CRITICA\": 2, \"ALTA\": 8}")
    private Map<String, Long> totalPorSeveridade;

    @Schema(description = "Somatório global de incidentes registrados no sistema", example = "17")
    private long totalGeral;
}