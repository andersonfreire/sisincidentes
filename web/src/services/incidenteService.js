import { request } from "./api";

const ENDPOINT = "/incidentes";

const preparePayload = (data) => ({
    titulo: data.titulo || "Sem título",
    descricao: data.descricao || "Sem descrição",
    status: data.status || "ABERTO",
    numeroChamado: data.numeroChamado || null,
    tarefaRelacionada: data.tarefaRelacionada || null,
    tipo: data.tipo || null,
    prioridade: data.prioridade || null,
    ipOrigem: data.ipOrigem || null,
    ipDestino: data.ipDestino || null,
    host: data.host || null,
    tempoEstimado: data.tempoEstimado || null,
    cc: data.cc || null,
    notas: data.notas || null,
    categoriaId: data.categoriaId ? Number(data.categoriaId) : null,
    unidadeId: data.unidadeId ? Number(data.unidadeId) : null,
    autorId: data.autorId ? Number(data.autorId) : null,
    atribuidoId: data.atribuidoId ? Number(data.atribuidoId) : null,
    vulnerabilidadesIds: data.vulnerabilidadesIds ? data.vulnerabilidadesIds.map(Number) : []
});

export const createIncidente = async (data) => {
    return request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(preparePayload(data)),
    });
};

export const updateIncidente = async (id, data) => {
    return request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(preparePayload(data)),
    });
};

export const getIncidentes = () => request(ENDPOINT);
export const getIncidenteById = (id) => request(`${ENDPOINT}/${id}`);
export const deleteIncidente = (id) => request(`${ENDPOINT}/${id}`, { method: "DELETE" });