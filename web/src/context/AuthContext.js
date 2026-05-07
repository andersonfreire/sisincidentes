import React, { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Efeito para carregar o usuário do localStorage ao iniciar
    useEffect(() => {
        const storedUser = localStorage.getItem("@SisIncidentes:user");
        const storedToken = localStorage.getItem("@SisIncidentes:token");

        if (storedUser && storedToken) {
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    // Função de Login via API Java
    const login = async (email, senha) => {
        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, senha }),
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.mensagem || "Credenciais inválidas.");
            }

            // Salva no estado e no localStorage
            const userData = data.usuario;
            const token = data.token;

            localStorage.setItem("@SisIncidentes:user", JSON.stringify(userData));
            localStorage.setItem("@SisIncidentes:token", token);
            
            setUser(userData);
            return { success: true };
        } catch (error) {
            return { success: false, error: error.message };
        }
    };

    // Função de Logout
    const logout = () => {
        localStorage.removeItem("@SisIncidentes:user");
        localStorage.removeItem("@SisIncidentes:token");
        setUser(null);
    };

    // Helper para obter o Token
    const getToken = () => localStorage.getItem("@SisIncidentes:token");

    return (
        <AuthContext.Provider value={{ user, loading, login, logout, getToken }}>
            {children}
        </AuthContext.Provider>
    );
};
