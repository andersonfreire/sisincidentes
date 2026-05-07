// Serviço de CRUD para Usuários.
// Integração com API REST Spring Boot (RF02).

const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";
const ENDPOINT = `${BASE_URL}/usuarios`;

// Helper para tratar respostas da API
const handleResponse = async (response) => {
    if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        const msg = errorBody.mensagem || `Erro HTTP ${response.status}`;
        throw new Error(msg);
    }
    // 204 No Content não tem body
    if (response.status === 204) return null;
    return response.json();
};

// Cadastrar
export const createUsuario = async (data) => {
    const response = await fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    const result = await handleResponse(response);
    return { ...result, success: true, message: "Usuário criado com sucesso!" };
};

// Atualizar
export const updateUsuario = async (id, updates) => {
    const response = await fetch(`${ENDPOINT}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updates),
    });
    const result = await handleResponse(response);
    return { ...result, success: true, message: "Usuário atualizado com sucesso!" };
};

// Consultar (listar todos)
export const getUsuarios = async () => {
    const response = await fetch(ENDPOINT);
    return handleResponse(response);
};

// Consultar (buscar por ID)
export const getUsuarioById = async (id) => {
    const response = await fetch(`${ENDPOINT}/${id}`);
    return handleResponse(response);
};

// Pesquisar por nome (parcial, case-insensitive)
export const searchByNome = async (nome) => {
    const response = await fetch(`${ENDPOINT}/buscar?nome=${encodeURIComponent(nome)}`);
    return handleResponse(response);
};

// Filtrar por unidade
export const getUsuariosByUnidade = async (unidadeId) => {
    const response = await fetch(`${ENDPOINT}/unidade/${unidadeId}`);
    return handleResponse(response);
};

// Ativar/Desativar
export const toggleAtivoUsuario = async (id) => {
    const response = await fetch(`${ENDPOINT}/${id}/toggle-ativo`, {
        method: "PATCH",
    });
    const result = await handleResponse(response);
    return { ...result, success: true, message: `Usuário ${result.ativo ? 'ativado' : 'desativado'} com sucesso!` };
};

// Excluir
export const deleteUsuario = async (id) => {
    const response = await fetch(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    await handleResponse(response);
    return { success: true, message: "Usuário excluído com sucesso!" };
};