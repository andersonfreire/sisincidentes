import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import Categoria from "./pages/Categoria/Categoria";
import UnidadeAdministrativa from "./pages/UnidadeAdministrativa/UnidadeAdministrativa";
import Usuario from "./pages/Usuario/Usuario";
import Home from "./pages/Home/Home";
import MainLayout from "./components/MainLayout/MainLayout";
import Login from "./pages/Login/Login";
import Incidente from "./pages/Incidente/Indicente";
import Estatisticas from "./pages/Estatisticas/Estatisticas";
import Licao from "./pages/Licao/Licao";
import Relatorios from "./pages/Relatorios/Relatorios";
import UserProfile from "./pages/Usuario/UserProfile";

const PrivateRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Carregando...</div>;
  }

  return user ? children : <Navigate to="/login" />;
};

const PublicRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Carregando...</div>;
  }

  return !user ? children : <Navigate to="/" />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route
            path="/login"
            element={
              <PublicRoute>
                <Login />
              </PublicRoute>
            }
          />

          <Route
            path="/"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Home />
                </MainLayout>
              </PrivateRoute>
              
            }
          />

          <Route path="/" element={<MainLayout />} />
          
          <Route
            path="/categorias"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Categoria />
                </MainLayout>
              </PrivateRoute>
            }
          />
          
          <Route
            path="/relatorios"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Relatorios />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route
            path="/unidades"
            element={
              <PrivateRoute>
                <MainLayout>
                  <UnidadeAdministrativa />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route 
            path="/estatisticas"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Estatisticas />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route
            path="/usuarios"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Usuario />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route 
            path="/incidentes"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Incidente />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route
            path="/licoes"
            element={
              <PrivateRoute>
                <MainLayout>
                  <Licao />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route
            path="/perfil"
            element={
              <PrivateRoute>
                <MainLayout>
                  <UserProfile />
                </MainLayout>
              </PrivateRoute>
            }
          />

          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
