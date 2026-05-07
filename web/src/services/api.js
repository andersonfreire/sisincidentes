const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

/**
 * Função utilitária para realizar requisições autenticadas.
 * Adiciona automaticamente o Token JWT do localStorage aos Headers.
 */
export const request = async (endpoint, options = {}) => {
    const token = localStorage.getItem("@SisIncidentes:token");
    
    const headers = {
        "Content-Type": "application/json",
        ...options.headers,
    };

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {
        ...options,
        headers,
    });

    if (response.status === 401 || response.status === 403) {
        // Token expirado ou sem permissão
        // localStorage.removeItem("@SisIncidentes:token");
        // window.location.href = "/login";
    }

    if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        const msg = errorBody.mensagem || `Erro HTTP ${response.status}`;
        throw new Error(msg);
    }

    if (response.status === 204) return null;
    return response.json();
};
