import { request } from "./api";

const ENDPOINT = "/incidentes";

// Cadastrar novo incidente
export const createIncidente = async (data) => {
    // Mapeamento para o DTO do Backend (IncidenteRequestDTO)
    const payload = {
        titulo: data.assunto || data.titulo || "Sem título",
        descricao: data.descricao || "Sem descrição",
        status: data.situacao || data.status || "ABERTO",
        numeroChamado: data.numeroChamado,
        tarefaRelacionada: data.tarefaRelacionada,
        tipo: data.tipo,
        prioridade: data.prioridade,
        ipOrigem: data.ipOrigem,
        ipDestino: data.ipDestino,
        host: data.host,
        tempoEstimado: data.tempoEstimado,
        cc: data.cc,
        notas: data.notas,
        categoriaId: data.categoriaId,
        unidadeId: data.unidadeId,
        autorId: data.autorId,
        atribuidoId: data.atribuidoId,
        vulnerabilidadesIds: data.vulnerabilidadesIds || []
    };

    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Incidente registrado com sucesso!" };
};

// Atualizar incidente existente
export const updateIncidente = async (id, updates) => {
    // Mapeamento para o DTO do Backend (IncidenteRequestDTO)
    const payload = {
        titulo: updates.assunto || updates.titulo,
        descricao: updates.descricao,
        status: updates.situacao || updates.status,
        numeroChamado: updates.numeroChamado,
        tarefaRelacionada: updates.tarefaRelacionada,
        tipo: updates.tipo,
        prioridade: updates.prioridade,
        ipOrigem: updates.ipOrigem,
        ipDestino: updates.ipDestino,
        host: updates.host,
        tempoEstimado: updates.tempoEstimado,
        cc: updates.cc,
        notas: updates.notas,
        categoriaId: updates.categoriaId,
        unidadeId: updates.unidadeId,
        autorId: updates.autorId,
        atribuidoId: updates.atribuidoId,
        vulnerabilidadesIds: updates.vulnerabilidadesIds || []
    };

    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Incidente atualizado com sucesso!" };
};

// Listar todos os incidentes
export const getIncidentes = async () => {
    return request(ENDPOINT);
};

// Buscar incidente por ID
export const getIncidenteById = async (id) => {
    return request(`${ENDPOINT}/${id}`);
};

// Excluir incidente
export const deleteIncidente = async (id) => {
    await request(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    return { success: true, message: "Incidente excluído com sucesso!" };
};
