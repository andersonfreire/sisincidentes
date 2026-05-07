package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.DashboardStatsDTO;
import com.seguranca.sisincidentes.domain.repository.IncidenteRepository;
import com.seguranca.sisincidentes.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementação concreta do serviço de relatórios.
 *
 * <p>Executa as agregações diretamente na camada de persistência (JPQL)
 * para otimizar o consumo de memória e o tempo de processamento.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RelatorioServiceImpl implements RelatorioService {

    private final IncidenteRepository incidenteRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO obterEstatisticasGerais() {
        log.debug("Iniciando extração de métricas e consolidação de estatísticas.");

        Map<String, Long> statsStatus = incidenteRepository.countIncidentesByStatus()
                .stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));

        Map<String, Long> statsSeveridade = incidenteRepository.countByVulnerabilidadeSeveridade()
                .stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));

        long totalIncidentes = incidenteRepository.count();

        log.debug("Métricas extraídas com sucesso. Total de incidentes computados: {}", totalIncidentes);

        return DashboardStatsDTO.builder()
                .totalPorStatus(statsStatus)
                .totalPorSeveridade(statsSeveridade)
                .totalGeral(totalIncidentes)
                .build();
    }
}