import { request } from "./api";

const ENDPOINT = "/categorias";

// Criar nova categoria
export const createCategory = async (data) => {
    // Mapeamento para o DTO do Backend (CategoriaRequestDTO)
    const payload = {
        nome: data.nome,
        descricao: data.descricao,
        tipo: data.tipo?.toUpperCase()
    };

    const result = await request(ENDPOINT, {
        method: "POST",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Categoria criada com sucesso!" };
};

// Atualizar categoria existente
export const updateCategory = async (id, updates) => {
    // Mapeamento para o DTO do Backend (CategoriaRequestDTO)
    const payload = {
        nome: updates.nome,
        descricao: updates.descricao,
        tipo: updates.tipo?.toUpperCase()
    };

    const result = await request(`${ENDPOINT}/${id}`, {
        method: "PUT",
        body: JSON.stringify(payload),
    });
    return { ...result, success: true, message: "Categoria atualizada com sucesso!" };
};

// Buscar todas as categorias
export const getCategories = async () => {
    return request(ENDPOINT);
};

// Buscar categorias por tipo (INCIDENTE ou VULNERABILIDADE)
export const getCategoriesByTipo = async (tipo) => {
    return request(`${ENDPOINT}/tipo/${tipo?.toUpperCase()}`);
};

// Buscar categoria por ID
export const getCategoryById = async (id) => {
    return request(`${ENDPOINT}/${id}`);
};

// Deletar categoria
export const deleteCategory = async (id) => {
    await request(`${ENDPOINT}/${id}`, {
        method: "DELETE",
    });
    return { success: true, message: "Categoria deletada com sucesso!" };
};
