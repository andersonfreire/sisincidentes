import React, { createContext, useContext, useEffect, useState } from "react";
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "../firebaseConfig";

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

// Usuário simulado para o modo de desenvolvimento (DEV_MODE)
// Remove ou defina REACT_APP_DEV_MODE=false ao ativar autenticação JWT completa
const DEV_USER = {
  uid: "dev-user-001",
  email: "dev@sisincidentes.gov.br",
  displayName: "Desenvolvedor",
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Modo DEV: simula usuário logado sem depender do Firebase Auth
    if (process.env.REACT_APP_DEV_MODE === "true") {
      setUser(DEV_USER);
      setLoading(false);
      return;
    }

    // Modo PRODUÇÃO: usa Firebase Authentication normalmente
    const unsub = onAuthStateChanged(auth, (u) => {
      setUser(u);
      setLoading(false);
    });
    return () => unsub();
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

