import React from "react";
import "./Header.css";
import { Dropdown, Image } from "react-bootstrap";
import { FaUserCircle, FaSignOutAlt } from "react-icons/fa";
import { signOut } from "firebase/auth";
import { auth } from "../../firebaseConfig";
import { useAuth } from "../../context/AuthContext";
import LogoUfrn from "../../assets/logo-UFRN.png";
import { NavLink } from "react-router-dom";

const Header = () => {
  const { user } = useAuth();

  const handleLogout = async () => {
    try {
      await signOut(auth);
    } catch (error) {
      console.error("Erro ao fazer logout:", error);
    }
  };

  return (
    <header className="header-component bg-dark text-white py-3 shadow-sm">
      <div className="container-fluid d-flex align-items-center justify-content-between px-3">
        <div className="d-flex align-items-center">
          <Image
            src={LogoUfrn}
            alt="Logo da UFRN"
            className="header-logo me-3"
            rounded
          />
          <h6 className="mb-0 text-uppercase fw-semibold">
            Universidade Federal do Rio Grande do Norte
          </h6>
        </div>

        {user && (
          <Dropdown align="end">
            <Dropdown.Toggle
              variant="outline-primary"
              id="dropdown-user"
              className="d-flex align-items-center"
            >
              <FaUserCircle className="me-2 fs-5" />
              <span>{user.email}</span>
            </Dropdown.Toggle>

               <Dropdown.Menu className="shadow-sm">
                {/* ✅ Agora usa NavLink, não recarrega a página */}
                <Dropdown.Item as={NavLink} to="/perfil">
                  <FaUserCircle className="me-2 text-primary" /> Perfil
                </Dropdown.Item>

                <Dropdown.Divider />

                <Dropdown.Item onClick={handleLogout} className="text-danger">
                  <FaSignOutAlt className="me-2" /> Sair
                </Dropdown.Item>
              </Dropdown.Menu>
          </Dropdown>
        )} 
      </div>
    </header>
  );
};

export default Header;
