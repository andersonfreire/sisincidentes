import { request } from "./api";

const ENDPOINT = "/licoes-aprendidas";

export const createLicao = async (data) => {
    const payload = {
        incidenteId: Number(data.incidenteId),
        descricaoResolucao: data.descricaoResolucao
    };

    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Lição aprendida registada com sucesso!" };
};

export const updateLicao = async (id, data) => {
    const payload = {
        incidenteId: Number(data.incidenteId),
        descricaoResolucao: data.descricaoResolucao
    };

    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Lição aprendida atualizada com sucesso!" };
};

export const getLicoes = () => request(ENDPOINT);
export const getLicaoByIncidenteId = (incidenteId) => request(`${ENDPOINT}/incidente/${incidenteId}`);
export const deleteLicao = (id) => request(`${ENDPOINT}/${id}`, { method: "DELETE" });