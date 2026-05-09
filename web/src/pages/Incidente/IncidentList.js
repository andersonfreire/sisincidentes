import React, { useEffect, useState } from "react";
import { Button, Accordion, Spinner, Badge } from "react-bootstrap";
import { deleteIncidente, getIncidentes } from "../../services/incidenteService";
import ConfirmModal from "../../components/ConfirmModal/ConfirmModal";

const IncidentList = ({ onEdit }) => {
    const [incidentes, setIncidentes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [incidenteSelecionado, setIncidenteSelecionado] = useState(null);

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
                <Accordion className="border-0 gap-2 d-flex flex-column">
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
                                
                                <div className="d-flex justify-content-end mt-3">
                                    <Button 
                                        variant="outline-dark" 
                                        size="sm" 
                                        className="rounded-0 shadow-none me-2" 
                                        onClick={() => onEdit(incidente)}
                                    >
                                        Editar
                                    </Button>
                                    <Button 
                                        variant="dark" 
                                        size="sm" 
                                        className="rounded-0 shadow-none" 
                                        onClick={() => handleDeleteClick(incidente)}
                                    >
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
                message={
                    incidenteSelecionado
                        ? `Confirma a exclusão do registo #${incidenteSelecionado.numeroChamado || incidenteSelecionado.id}?`
                        : ""
                }
            />
        </div>
    );
};

export default IncidentList;