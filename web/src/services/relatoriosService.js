import { request } from "./api";
import { getCategories } from "./categoriaService";
import { getUnidades } from "./unidadeAdministrativaService";
import { getIncidentes } from "./incidenteService";

export const getRelatoriosData = async (startDate, endDate) => {
    const [allIncidents, categories, unidades] = await Promise.all([
        getIncidentes(),
        getCategories(),
        getUnidades(),
    ]);

    const incidentesValidos = Array.isArray(allIncidents) ? allIncidents : [];
    const categoriasValidas = Array.isArray(categories) ? categories : [];
    const unidadesValidas = Array.isArray(unidades) ? unidades : [];

    // Filtro temporal robusto
    const filteredIncidents = incidentesValidos.filter(i => {
        const dataReferencia = i.dataRegistro || i.createdAt;
        if (!dataReferencia) return true;
        
        const creationDate = new Date(dataReferencia);
        if (isNaN(creationDate)) return true;

        return creationDate >= startDate && creationDate <= endDate;
    });

    // Sanitização de strings para comparação
    const normalizar = (str) => (str || "").toUpperCase().trim();

    const open = filteredIncidents.filter(i => 
        ["ABERTO", "ABERTA"].includes(normalizar(i.status) || normalizar(i.situacao))
    );
    
    const ongoing = filteredIncidents.filter(i => 
        ["EM_ANALISE", "EM ANDAMENTO", "EM_ANDAMENTO"].includes(normalizar(i.status) || normalizar(i.situacao))
    );
    
    const completed = filteredIncidents.filter(i => 
        ["RESOLVIDO", "CONCLUIDO", "CONCLUÍDO", "CONCLUÍDA"].includes(normalizar(i.status) || normalizar(i.situacao))
    );

    const isInc = (i) => normalizar(i.tipo) === "INCIDENTE";
    const isVul = (i) => normalizar(i.tipo) === "VULNERABILIDADE";

    const openIncidents = open.filter(isInc).length;
    const openVulnerabilities = open.filter(isVul).length;
    const ongoingIncidents = ongoing.filter(isInc).length;
    const ongoingVulnerabilities = ongoing.filter(isVul).length;
    const completedIncidents = completed.filter(isInc).length;
    const completedVulnerabilities = completed.filter(isVul).length;

    const totalIncidentsCount = filteredIncidents.filter(isInc).length;
    const totalVulnerabilitiesCount = filteredIncidents.filter(isVul).length;

    const completedIncidentsPercentage = totalIncidentsCount > 0 ? (completedIncidents / totalIncidentsCount) * 100 : 0;
    const completedVulnerabilitiesPercentage = totalVulnerabilitiesCount > 0 ? (completedVulnerabilities / totalVulnerabilitiesCount) * 100 : 0;

    // Agrupamento por Categoria com suporte a objetos aninhados
    const categoryCounts = filteredIncidents.reduce((acc, incident) => {
        const catId = incident.categoriaId || (incident.categoria && incident.categoria.id);
        if (catId) acc[catId] = (acc[catId] || 0) + 1;
        return acc;
    }, {});

    const categoryData = Object.keys(categoryCounts).map(id => {
        const category = categoriasValidas.find(c => String(c.id) === String(id));
        return {
            name: category ? category.nome : "Sem Categoria",
            "Incidentes/Vulnerabilidades": categoryCounts[id],
        };
    });

    // Agrupamento por Unidade Administrativa
    const unitCounts = filteredIncidents.reduce((acc, incident) => {
        const unitId = incident.unidadeId || (incident.unidade && incident.unidade.id);
        if (unitId) acc[unitId] = (acc[unitId] || 0) + 1;
        return acc;
    }, {});

    const unitData = Object.keys(unitCounts).map(id => {
        const unidade = unidadesValidas.find(u => String(u.id) === String(id));
        return {
            name: unidade ? unidade.sigla : "Sem Unidade",
            "Incidentes/Vulnerabilidades": unitCounts[id],
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

export const getDashboardStats = async () => request("/relatorios/estatisticas");