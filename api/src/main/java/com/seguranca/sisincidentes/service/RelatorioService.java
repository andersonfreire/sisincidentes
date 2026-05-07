package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.DashboardStatsDTO;

/**
 * Interface de serviço para o módulo de Relatórios e Estatísticas.
 *
 * <p>Define o contrato de negócio para extração de métricas de segurança,
 * garantindo o desacoplamento da API REST e facilitando a implementação de
 * diferentes estratégias de agregação de dados.</p>
 *
 * <p><b>RF07 — Relatórios / RF09 — Estatísticas</b></p>
 */
public interface RelatorioService {

    /**
     * Calcula e consolida os indicadores globais do sistema.
     *
     * @return DTO contendo sumarizações por status, severidade e o volume total.
     */
    DashboardStatsDTO obterEstatisticasGerais();
}