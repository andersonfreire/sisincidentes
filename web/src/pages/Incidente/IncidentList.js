import React, { useEffect, useState } from "react";
import { Button, Accordion, Spinner, Badge } from "react-bootstrap";
import { deleteIncidente, getIncidentes } from "../../services/incidenteService";
import { getInteracoesPorIncidente } from "../../services/interacaoService"; // Importe o novo serviço
import ConfirmModal from "../../components/ConfirmModal/ConfirmModal";

const IncidentList = ({ onEdit }) => {
    const [incidentes, setIncidentes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [incidenteSelecionado, setIncidenteSelecionado] = useState(null);
    
    // Estados para a RF10
    const [interacoes, setInteracoes] = useState({});
    const [loadingInteracoes, setLoadingInteracoes] = useState({});

    const fetchIncidentes = async () => {
        setLoading(true);
        try {
            const data = await getIncidentes();
            setIncidentes(data || []);
        } catch (error) {
            console.error("Erro ao carregar incidentes", error);
        } finally {
            setLoading(false);
        }
    };

    // Função para carregar histórico ao expandir
    const fetchHistorico = async (id) => {
        if (!id || interacoes[id]) return;
        setLoadingInteracoes(prev => ({ ...prev, [id]: true }));
        try {
            const data = await getInteracoesPorIncidente(id);
            setInteracoes(prev => ({ ...prev, [id]: data }));
        } catch (error) {
            console.error("Erro ao carregar histórico", error);
        } finally {
            setLoadingInteracoes(prev => ({ ...prev, [id]: false }));
        }
    };

    useEffect(() => {
        fetchIncidentes();
    }, []);

    const handleDeleteClick = (incidente) => {
        setIncidenteSelecionado(incidente);
        setShowModal(true);
    };

    const handleConfirmDelete = async () => {
        if (incidenteSelecionado) {
            await deleteIncidente(incidenteSelecionado.id);
            setShowModal(false);
            setIncidenteSelecionado(null);
            fetchIncidentes();
        }
    };

    const handleCancelDelete = () => {
        setShowModal(false);
        setIncidenteSelecionado(null);
    };

    const formatarData = (dataString) => {
        if (!dataString) return "—";
        return new Date(dataString).toLocaleString("pt-PT");
    };

    return (
        <div>
            {loading ? (
                <div className="text-center p-4">
                    <Spinner animation="border" variant="dark" />
                </div>
            ) : (
                <Accordion 
                    className="border-0 gap-2 d-flex flex-column"
                    onSelect={(id) => fetchHistorico(id)} // Disparador da RF10
                >
                    {incidentes.map((incidente) => (
                        <Accordion.Item 
                            key={incidente.id} 
                            eventKey={incidente.id.toString()} 
                            className="border border-dark rounded-0 shadow-none bg-light"
                        >
                            <Accordion.Header className="shadow-none">
                                <strong>#{incidente.numeroChamado || incidente.id}</strong> — {incidente.titulo} 
                                <Badge bg="dark" className="ms-3 rounded-0 shadow-none">
                                    {incidente.status}
                                </Badge>
                            </Accordion.Header>
                            <Accordion.Body className="bg-white border-top border-dark rounded-0">
                                <p><strong>Descrição:</strong> {incidente.descricao}</p>
                                <p><strong>Data de Registo:</strong> {formatarData(incidente.dataRegistro)}</p>
                                <p><strong>Tipo:</strong> {incidente.tipo || "—"}</p>
                                <p><strong>Prioridade:</strong> {incidente.prioridade || "—"}</p>
                                
                                {/* TIMELINE - RF10 */}
                                <div className="mt-4 border-top border-dark pt-3">
                                    <h6 className="text-uppercase fw-bold mb-3" style={{ fontSize: '0.75rem' }}>Histórico de Tramitação</h6>
                                    {loadingInteracoes[incidente.id] ? (
                                        <Spinner animation="border" size="sm" variant="dark" />
                                    ) : (
                                        <div className="d-flex flex-column gap-2">
                                            {interacoes[incidente.id]?.length > 0 ? (
                                                interacoes[incidente.id].map(it => (
                                                    <div key={it.id} className="ps-2 border-start border-dark py-1">
                                                        <div className="fw-bold" style={{ fontSize: '0.7rem' }}>{formatarData(it.dataCriacao)}</div>
                                                        <div className="small text-secondary">{it.texto}</div>
                                                    </div>
                                                ))
                                            ) : <span className="text-muted small">Sem interações registadas.</span>}
                                        </div>
                                    )}
                                </div>

                                <div className="d-flex justify-content-end mt-3 border-top pt-2">
                                    <Button variant="outline-dark" size="sm" className="rounded-0 shadow-none me-2" onClick={() => onEdit(incidente)}>
                                        Editar
                                    </Button>
                                    <Button variant="dark" size="sm" className="rounded-0 shadow-none" onClick={() => handleDeleteClick(incidente)}>
                                        Eliminar
                                    </Button>
                                </div>
                            </Accordion.Body>
                        </Accordion.Item>
                    ))}
                </Accordion>
            )}

            <ConfirmModal
                show={showModal}
                onHide={handleCancelDelete}
                onConfirm={handleConfirmDelete}
                title="Confirmar exclusão"
                message={incidenteSelecionado ? `Confirma a exclusão do registo #${incidenteSelecionado.numeroChamado || incidenteSelecionado.id}?` : ""}
            />
        </div>
    );
};

export default IncidentList;