import { request } from "./api";

const ENDPOINT = "/licoes-aprendidas";

// Registrar nova lição aprendida
export const createLicao = async (data) => {
    // Mapeamento para o DTO do Backend (LicaoAprendidaRequestDTO)
    const payload = {
        incidenteId: data.id_incidente || data.incidenteId,
        descricaoResolucao: data.descricao || data.descricaoResolucao
    };

    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Lição aprendida registrada com sucesso!" };
};

// Atualizar lição aprendida existente
export const updateLicao = async (id, data) => {
    // Mapeamento para o DTO do Backend (LicaoAprendidaRequestDTO)
    const payload = {
        incidenteId: data.id_incidente || data.incidenteId,
        descricaoResolucao: data.descricao || data.descricaoResolucao
    };

    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Lição aprendida atualizada com sucesso!" };
};

// Listar todas as lições aprendidas
export const getLicoes = async () => {
    return request(ENDPOINT);
};

// Buscar lição por ID do Incidente
export const getLicaoByIncidenteId = async (incidenteId) => {
    return request(`${ENDPOINT}/incidente/${incidenteId}`);
};

// Excluir lição aprendida
export const deleteLicao = async (id) => {
    await request(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    return { success: true, message: "Lição excluída com sucesso!" };
};
