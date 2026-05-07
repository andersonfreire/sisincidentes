import React, { useState } from "react";
import { Nav } from "react-bootstrap";
import { Link } from "react-router-dom";

import {
  FaHome,
  FaUsers,
  FaBuilding,
  FaListAlt,
  FaBug,
  FaLightbulb,
  FaChartBar,
  FaBars,
  FaTimes,
} from "react-icons/fa";
import "./NavBar.css";

const NavBar = () => {
  const [isOpen, setIsOpen] = useState(true);

  const toggleSidebar = () => setIsOpen(!isOpen);

  return (
    <>
      {/* Sidebar */}
      <div
        className={`sidebar bg-dark text-light d-flex flex-column p-3 ${
          isOpen ? "open" : "collapsed"
        }`}
      >
        {/* Cabeçalho com botão de collapse sempre visível */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <button
            className="btn btn-dark border-0 p-2 toggle-btn"
            onClick={toggleSidebar}
            aria-label="Alternar menu"
          >
            {isOpen ? (
                <span className="d-flex align-items-center justify-content-center gap-3">
                   <h4 className="m-0"> SisInformes </h4>  <FaBars size={18} />
                </span>
                ) : (
                <FaBars size={18} />
                )}

          </button>
        </div>

        {/* Links de navegação */}
        <Nav className="flex-column">
          <Nav.Link as={Link} to="/home" className="text-light sidebar-item">
            <FaHome className="me-2" /> {isOpen && "Home"}
          </Nav.Link>

          <Nav.Link as={Link} to="/unidades" className="text-light sidebar-item">
            <FaBuilding className="me-2" /> {isOpen && "Unidades Administrativas"}
          </Nav.Link>

          <Nav.Link as={Link} to="/usuarios" className="text-light sidebar-item">
            <FaUsers className="me-2" /> {isOpen && "Usuários"}
          </Nav.Link>

          <Nav.Link as={Link} to="/categorias" className="text-light sidebar-item">
            <FaListAlt className="me-2" /> {isOpen && "Categorias"}
          </Nav.Link>

          <Nav.Link as={Link} to="/incidentes" className="text-light sidebar-item">
            <FaBug className="me-2" /> {isOpen && "Incidentes e Vulnerabilidades"}
          </Nav.Link>

          <Nav.Link as={Link} to="/relatorios" className="text-light sidebar-item">
            <FaChartBar className="me-2" /> {isOpen && "Relatórios"}
          </Nav.Link>

          <Nav.Link as={Link} to="/licoes" className="text-light sidebar-item">
            <FaLightbulb className="me-2" /> {isOpen && "Lições"}
          </Nav.Link>

          <Nav.Link as={Link} to="/estatisticas" className="text-light sidebar-item">
            <FaChartBar className="me-2" /> {isOpen && "Estatísticas da ANPD"}
          </Nav.Link>
        </Nav>
      </div>
    </>
  );
};

export default NavBar;
