import { request } from "./api";
import { getCategories } from "./categoriaService";
import { getUnidades } from "./unidadeAdministrativaService";
import { getIncidentes } from "./incidenteService";

/**
 * Busca dados para o dashboard e relatórios.
 * Realiza as chamadas REST e processa os dados localmente para manter a compatibilidade com a UI existente.
 */
export const getRelatoriosData = async (startDate, endDate) => {
    // 1. Buscar dados via REST
    const [allIncidents, categories, unidades] = await Promise.all([
        getIncidentes(),
        getCategories(),
        getUnidades(),
    ]);

    // 2. Filtrar por data (opcional, dependendo da UI)
    const filteredIncidents = allIncidents.filter(i => {
        if (!i.dataRegistro) return true; // Se não tiver data, mantemos (ou filtramos fora)
        const creationDate = new Date(i.dataRegistro);
        return creationDate >= startDate && creationDate <= endDate;
    });

    // 3. Processamento de estatísticas (Compatibilidade com a UI legada)
    const open = filteredIncidents.filter(i => i.status === "ABERTO" || i.status === "Aberta");
    const ongoing = filteredIncidents.filter(i => i.status === "EM_ANALISE" || i.status === "Em Andamento");
    const completed = filteredIncidents.filter(i => i.status === "RESOLVIDO" || i.status === "Concluída");

    const openIncidents = open.filter(i => i.tipo === "Incidente").length;
    const openVulnerabilities = open.filter(i => i.tipo === "Vulnerabilidade").length;

    const ongoingIncidents = ongoing.filter(i => i.tipo === "Incidente").length;
    const ongoingVulnerabilities = ongoing.filter(i => i.tipo === "Vulnerabilidade").length;
    
    const completedIncidents = completed.filter(i => i.tipo === "Incidente").length;
    const completedVulnerabilities = completed.filter(i => i.tipo === "Vulnerabilidade").length;

    const totalIncidentsCount = filteredIncidents.filter(i => i.tipo === "Incidente").length;
    const totalVulnerabilitiesCount = filteredIncidents.filter(i => i.tipo === "Vulnerabilidade").length;

    const completedIncidentsPercentage = totalIncidentsCount > 0 ? (completedIncidents / totalIncidentsCount) * 100 : 0;
    const completedVulnerabilitiesPercentage = totalVulnerabilitiesCount > 0 ? (completedVulnerabilities / totalVulnerabilitiesCount) * 100 : 0;

    // Agrupamento por Categoria
    const categoryCounts = filteredIncidents.reduce((acc, incident) => {
        const categoryId = incident.categoriaId; // Verificar se o backend retorna IDs ou objetos
        if (categoryId) {
            acc[categoryId] = (acc[categoryId] || 0) + 1;
        }
        return acc;
    }, {});

    const categoryData = Object.keys(categoryCounts).map(categoryId => {
        const category = categories.find(c => String(c.id) === String(categoryId));
        return {
            name: category ? category.nome : "Sem Categoria",
            "Incidentes/Vulnerabilidades": categoryCounts[categoryId],
        };
    });

    // Agrupamento por Unidade
    const unitCounts = filteredIncidents.reduce((acc, incident) => {
        const unitId = incident.unidadeId;
        if (unitId) {
            acc[unitId] = (acc[unitId] || 0) + 1;
        }
        return acc;
    }, {});

    const unitData = Object.keys(unitCounts).map(unitId => {
        const unidade = unidades.find(u => String(u.id) === String(unitId));
        return {
            name: unidade ? unidade.sigla : "Sem Unidade",
            "Incidentes/Vulnerabilidades": unitCounts[unitId],
        };
    });

    return {
        stats: {
            openIncidents,
            openVulnerabilities,
            ongoingIncidents,
            ongoingVulnerabilities,
            completedIncidentsPercentage,
            completedVulnerabilitiesPercentage,
        },
        categoryData,
        unitData,
    };
};

/**
 * Busca estatísticas consolidadas diretamente do novo endpoint do backend.
 */
export const getDashboardStats = async () => {
    return request("/relatorios/estatisticas");
};
