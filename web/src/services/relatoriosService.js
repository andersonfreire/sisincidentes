import { collection, query, where, getDocs } from "firebase/firestore";
import { db } from "../firebaseConfig";
import { getCategories } from "./categoryService";
import { getUnidades } from "./unidadeAdministrativaService";

const incidentCollection = collection(db, "incidentes");

export const getAllIncidents = async () => {
    const snapshot = await getDocs(query(incidentCollection));
    return snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
};

export const getRelatoriosData = async (startDate, endDate) => {
    const [allIncidents, categories, unidades] = await Promise.all([
        getAllIncidents(),
        getCategories(),
        getUnidades(),
    ]);

    const filteredIncidents = allIncidents.filter(i => {
        if (!i.createdAt) return false;
        const creationDate = i.createdAt.toDate();
        return creationDate >= startDate && creationDate <= endDate;
    });

    const open = filteredIncidents.filter(i => i.situacao === "Aberta");
    const ongoing = filteredIncidents.filter(i => i.situacao === "Em Andamento");
    const completed = filteredIncidents.filter(i => i.situacao === "Concluída");

    const openIncidents = open.filter((i) => i.tipo === "Incidente").length;
    const openVulnerabilities = open.filter((i) => i.tipo === "Vulnerabilidade").length;

    const ongoingIncidents = ongoing.filter((i) => i.tipo === "Incidente").length;
    const ongoingVulnerabilities = ongoing.filter((i) => i.tipo === "Vulnerabilidade").length;
    
    const totalIncidents = openIncidents + ongoingIncidents + completed.filter((i) => i.tipo === "Incidente").length;
    const totalVulnerabilities = openVulnerabilities + ongoingVulnerabilities + completed.filter((i) => i.tipo === "Vulnerabilidade").length;

    const completedIncidentsPercentage = totalIncidents > 0 ? (completed.filter((i) => i.tipo === "Incidente").length / totalIncidents) * 100 : 0;
    const completedVulnerabilitiesPercentage = totalVulnerabilities > 0 ? (completed.filter((i) => i.tipo === "Vulnerabilidade").length / totalVulnerabilities) * 100 : 0;

    const categoryCounts = filteredIncidents.reduce((acc, incident) => {
        const categoryId = incident.categoriaId;
        if (categoryId) {
            if (!acc[categoryId]) {
                acc[categoryId] = 0;
            }
            acc[categoryId]++;
        }
        return acc;
    }, {});

    const categoryData = Object.keys(categoryCounts).map(categoryId => {
        const category = categories.find(c => c.id === categoryId);
        return {
            name: category ? category.nome : "Sem Categoria",
            "Incidentes/Vulnerabilidades": categoryCounts[categoryId],
        };
    });

    const unitCounts = filteredIncidents.reduce((acc, incident) => {
        const unitId = incident.unidadeId;
        if (unitId) {
            if (!acc[unitId]) {
                acc[unitId] = 0;
            }
            acc[unitId]++;
        }
        return acc;
    }, {});

    const unitData = Object.keys(unitCounts).map(unitId => {
        const unidade = unidades.find(u => u.id === unitId);
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
