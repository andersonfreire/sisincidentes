import { request } from "./api";

const ENDPOINT = "/usuarios";

// Cadastrar
export const createUsuario = async (data) => {
    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(data),
    });
    return { ...result, success: true, message: "Usuário criado com sucesso!" };
};

// Atualizar
export const updateUsuario = async (id, updates) => {
    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(updates),
    });
    return { ...result, success: true, message: "Usuário atualizado com sucesso!" };
};

// Consultar (listar todos)
export const getUsuarios = async () => {
    return request(ENDPOINT);
};

// Consultar (buscar por ID)
export const getUsuarioById = async (id) => {
    return request(`${ENDPOINT}/${id}`);
};

// Pesquisar por nome
export const searchByNome = async (nome) => {
    return request(`${ENDPOINT}/buscar?nome=${encodeURIComponent(nome)}`);
};

// Filtrar por unidade
export const getUsuariosByUnidade = async (unidadeId) => {
    return request(`${ENDPOINT}/unidade/${unidadeId}`);
};

// Ativar/Desativar
export const toggleAtivoUsuario = async (id) => {
    const result = await request(`${ENDPOINT}/${id}/toggle-ativo`, {
        method: "PATCH",
    });
    return { ...result, success: true, message: `Usuário ${result.ativo ? 'ativado' : 'desativado'} com sucesso!` };
};

// Excluir
export const deleteUsuario = async (id) => {
    await request(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    return { success: true, message: "Usuário excluído com sucesso!" };
};