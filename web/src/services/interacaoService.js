import { request } from "./api";

const ENDPOINT = "/interacoes";

export const getInteracoesPorIncidente = (incidenteId) => 
    request(`${ENDPOINT}/incidente/${incidenteId}`);

export const createInteracao = (incidenteId, texto) => {
    const payload = {
        incidenteId: Number(incidenteId),
        texto: texto
    };
    return request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(payload),
    });
};