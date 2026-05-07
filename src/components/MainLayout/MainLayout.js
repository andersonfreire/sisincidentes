import React from "react";
import Header from "../Header/Header";
import NavBar from "../NavBar/NavBar";

const MainLayout = ({ children }) => {
  return (
    <div className="app-layout d-flex">
      {/* Sidebar */}
      <NavBar />

      {/* Área principal */}
      <div className="main-content flex-grow-1">
        <Header />
        <div className="content p-3">{children}</div>
      </div>
    </div>
  );
};

export default MainLayout;
