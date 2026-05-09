import React from "react";

const TimelineInteracoes = ({ interacoes }) => {
    return (
        <div className="mt-3 border-top border-dark pt-3">
            <h6 className="text-uppercase fw-bold mb-3" style={{ fontSize: '0.75rem', letterSpacing: '1px' }}>
                Histórico de Tramitação
            </h6>
            {interacoes.length === 0 ? (
                <p className="text-muted small">Nenhuma interação registada.</p>
            ) : (
                <div className="d-flex flex-column gap-3">
                    {interacoes.map((item) => (
                        <div key={item.id} className="ps-3 border-start border-dark py-1">
                            <div className="d-flex justify-content-between align-items-center">
                                <span className="fw-bold small">
                                    {new Date(item.dataCriacao).toLocaleString('pt-PT')}
                                </span>
                            </div>
                            <div className="text-secondary mt-1" style={{ fontSize: '0.9rem' }}>
                                {item.texto}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default TimelineInteracoes;