import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import logoUFRN from "../../assets/logo-UFRN.png";
import "./Home.css";
import { useAuth } from "../../context/AuthContext";
import { getUsuarioById } from "../../services/usuarioService";

const Home = () => {

  const {user} = useAuth()
  const [usuario, setUsuario] = useState(null)

  useEffect(() => {
    const fetchUser = async () => {
      try {
        if (user) {
          const data = await getUsuarioById(user?.uid)
          setUsuario(data)
        }
      } catch (error) {
        console.error("Erro ao carregar unidades: ", error);
      }
      
    }

    fetchUser()
  }, [])

  return (
    <div className="min-vh-100 d-flex flex-column">

      {/* ===== Header ===== */}
      {/* <header className="home-header py-3 shadow-sm">
        <div className="container d-flex align-items-center justify-content-center flex-wrap">
          <img
            src={logoUFRN}
            alt="Logo da UFRN"
            className="logo-ufrn me-3"
          />
          <h5 className="mb-0 text-center text-uppercase">
            Universidade Federal do Rio Grande do Norte
          </h5>
        </div>
      </header> */}

      {/* ===== Conteúdo principal ===== */}
      <main className="flex-grow-1 d-flex align-items-center justify-content-center flex-column home-main">
        <div className="container text-center py-5">
          { user ? (
            <div> 
              <h1 className="display-5 fw-bold text-dark mb-3">
                Olá <span className="text-primary">{usuario?.nome?.split(" ")[0]}</span> !
              </h1>

              <h2> Seja bem-vindo ao <span className="text-primary">SisIncidentes</span> 🚨</h2>
            </div>
            
          ) : (
            <h1 className="display-5 fw-bold text-dark mb-3">
              Bem-vindo ao <span className="text-primary">SisIncidentes</span> 🚨
          </h1>
          )}
          
          <p className="lead text-muted mb-4">
            O SisIncidentes é um sistema web desenvolvido para registrar, visualizar e
            gerenciar incidentes de forma simples, eficiente e organizada.
          </p>
        </div>

        <div className="row justify-content-center mb-4 w-100 px-3 home-cards-row">
          {/* Bloco 1 - Recursos */}
          <div className="col-md-4 mb-3">
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <h5 className="card-title fw-semibold text-primary mb-3">
                  Principais Recursos
                </h5>
                <ul
                  className="list-unstyled text-start mx-auto feature-list"
                >
                  <li>📋 Cadastro, edição e exclusão de incidentes</li>
                  <li>🗂️ Gerenciamento completo de categorias</li>
                  <li>🔍 Filtros por nome e categoria</li>
                  <li>💾 Armazenamento em nuvem com Firebase Firestore</li>
                  <li>🔐 Autenticação segura</li>
                </ul>
              </div>
            </div>
          </div>

          {/* Bloco 2 - Tecnologias */}
          <div className="col-md-4 mb-3">
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <h5 className="card-title fw-semibold text-primary mb-3">
                  Tecnologias Utilizadas
                </h5>
                <ul
                  className="list-unstyled text-start mx-auto feature-list"
                >
                  <li>⚛️ <strong>React.js</strong> — construção da interface e componentes dinâmicos.</li>
                  <li>🧭 <strong>React Router DOM</strong> — controle de rotas e navegação.</li>
                  <li>🎨 <strong>Bootstrap 5</strong> — estilização moderna e responsiva.</li>
                  <li>🔥 <strong>Firebase Firestore</strong> — armazenamento de dados em nuvem.</li>
                  <li>🔐 <strong>Firebase Auth</strong> — autenticação de usuários.</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </main>

      {/* ===== Rodapé ===== */}
      <footer className="bg-white text-center text-muted py-3 border-top mt-auto">
        <small>© {new Date().getFullYear()} SisIncidentes</small>
      </footer>
    </div>
  );
};

export default Home;
