// Serviço de CRUD para Unidades Administrativas.
// Integração com API REST Spring Boot (RF01).

const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";
const ENDPOINT = `${BASE_URL}/unidades`;

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
export const createUnidade = async (data) => {
    const response = await fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    const result = await handleResponse(response);
    return { ...result, success: true, message: "Unidade criada com sucesso!" };
};

// Atualizar
export const updateUnidade = async (id, updates) => {
    const response = await fetch(`${ENDPOINT}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updates),
    });
    const result = await handleResponse(response);
    return { ...result, success: true, message: "Unidade atualizada com sucesso!" };
};

// Consultar (listar todas)
export const getUnidades = async () => {
    const response = await fetch(ENDPOINT);
    return handleResponse(response);
};

// Consultar (buscar por ID)
export const getUnidadeById = async (id) => {
    const response = await fetch(`${ENDPOINT}/${id}`);
    return handleResponse(response);
};

// Pesquisar por sigla (parcial, case-insensitive)
export const searchBySigla = async (sigla) => {
    const response = await fetch(`${ENDPOINT}/buscar?sigla=${encodeURIComponent(sigla)}`);
    return handleResponse(response);
};

// Pesquisar por título (parcial, case-insensitive)
export const searchByTitulo = async (titulo) => {
    const response = await fetch(`${ENDPOINT}/buscar?titulo=${encodeURIComponent(titulo)}`);
    return handleResponse(response);
};

// Excluir
export const deleteUnidade = async (id) => {
    const response = await fetch(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    await handleResponse(response);
    return { success: true, message: "Unidade deletada com sucesso!" };
};