import { request } from "./api";

const ENDPOINT = "/unidades";

// Cadastrar
export const createUnidade = async (data) => {
    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(data),
    });
    return { ...result, success: true, message: "Unidade criada com sucesso!" };
};

// Atualizar
export const updateUnidade = async (id, updates) => {
    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(updates),
    });
    return { ...result, success: true, message: "Unidade atualizada com sucesso!" };
};

// Consultar (listar todas)
export const getUnidades = async () => {
    return request(ENDPOINT);
};

// Consultar (buscar por ID)
export const getUnidadeById = async (id) => {
    return request(`${ENDPOINT}/${id}`);
};

// Pesquisar por sigla
export const searchBySigla = async (sigla) => {
    return request(`${ENDPOINT}/buscar?sigla=${encodeURIComponent(sigla)}`);
};

// Pesquisar por título
export const searchByTitulo = async (titulo) => {
    return request(`${ENDPOINT}/buscar?titulo=${encodeURIComponent(titulo)}`);
};

// Excluir
export const deleteUnidade = async (id) => {
    await request(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    return { success: true, message: "Unidade deletada com sucesso!" };
};