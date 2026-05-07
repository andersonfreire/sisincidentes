import React from 'react';
import './Estatisticas.css';

const Estatisticas = () => {
  return (
    <div className="estatisticas-container">
      <iframe
        title="Dashboard de Incidentes"
        width="100%"
        height="100%"
        src="https://app.powerbi.com/view?r=eyJrIjoiN2QxOGJmMTUtMjBmMi00YjdlLTkxMDEtM2U2ZTJkN2I3Mjc3IiwidCI6IjVhYmEwNGExLWY4NjMtNGI1Ni04MTdkLTQ0MjkxYzkwZDFiOCJ9"
        allowFullScreen="true"
      ></iframe>
    </div>
  );
};

export default Estatisticas;