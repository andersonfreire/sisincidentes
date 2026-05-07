import React, { useEffect, useState } from "react";
import { Button, Accordion, Spinner } from "react-bootstrap";
import { deleteIncidente, getIncidentes } from "../../services/incidentService";
import ConfirmModal from "../../components/ConfirmModal/ConfirmModal";
import { getCategoryById } from "../../services/categoryService";
import { getUnidadeById } from "../../services/unidadeAdministrativaService";
import { getUsuarioById } from "../../services/usuarioService";

const IncidenteList = ({ onEdit }) => {
    const [incidentes, setIncidentes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [incidenteSelecionado, setIncidenteSelecionado] = useState(null);

    // Busca todos os incidentes e enriquece com dados relacionados
    const fetchIncidentes = async () => {
        setLoading(true);
        try {
            const data = await getIncidentes();

            // Enriquecer incidentes com dados de categoria, unidade e usuários
            const incidentesComDetalhes = await Promise.all(
                data.map(async (incidente) => {
                    const [categoria, unidade, autor, atribuido] = await Promise.all([
                        incidente.categoriaId ? getCategoryById(incidente.categoriaId) : null,
                        incidente.unidadeId ? getUnidadeById(incidente.unidadeId) : null,
                        incidente.autorId ? getUsuarioById(incidente.autorId) : null,
                        incidente.atribuidoId ? getUsuarioById(incidente.atribuidoId) : null,
                    ]);

                    return {
                        ...incidente,
                        categoriaNome: categoria?.nome || "—",
                        unidadeNome: unidade?.titulo || "—",
                        autorNome: autor?.nome || "—",
                        atribuidoNome: atribuido?.nome || "—",
                    };
                })
            );

            setIncidentes(incidentesComDetalhes);
        } catch (error) {
            console.error("Erro ao buscar incidentes:", error);
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
        try {
            await deleteIncidente(incidenteSelecionado.id);
            setShowModal(false);
            setIncidenteSelecionado(null);
            fetchIncidentes();
        } catch (error) {
            console.error("Erro ao excluir incidente:", error);
        }
    };

    const handleCancelDelete = () => {
        setShowModal(false);
        setIncidenteSelecionado(null);
    };

    if (loading) {
        return (
            <div className="text-center my-5">
                <Spinner animation="border" role="status" />
                <div>Carregando incidentes...</div>
            </div>
        );
    }

    return (
        <div className="py-3">
            <h5 className="mb-3">Registros</h5>

            {incidentes.length === 0 ? (
                <p className="text-center text-muted">Nenhum incidente registrado.</p>
            ) : (
                <Accordion defaultActiveKey={null}>
                    {incidentes.map((incidente, index) => (
                        <Accordion.Item eventKey={index.toString()} key={incidente.id}>
                            <Accordion.Header>
                                <div className="d-flex flex-column flex-md-row w-100 justify-content-between">
                                    <div>
                                        <strong>#{incidente.numeroChamado || "—"} </strong>
                                        — {incidente.assunto || "(sem assunto)"}
                                    </div>
                                    <div className="text-muted small">
                                        {incidente.situacao} • {incidente.prioridade}
                                    </div>
                                </div>
                            </Accordion.Header>

                            <Accordion.Body>
                                <div className="p-2">
                                    <p><strong>Tipo:</strong> {incidente.tipo || "—"}</p>
                                    <p><strong>Categoria:</strong> {incidente.categoriaNome}</p>
                                    <p><strong>Unidade Administrativa:</strong> {incidente.unidadeNome}</p>
                                    <p><strong>Autor:</strong> {incidente.autorNome}</p>
                                    <p><strong>Atribuído para:</strong> {incidente.atribuidoNome}</p>

                                    <hr />

                                    <p><strong>IP de Origem:</strong> {incidente.ipOrigem || "—"}</p>
                                    <p><strong>IP de Destino:</strong> {incidente.ipDestino || "—"}</p>
                                    <p><strong>Host:</strong> {incidente.host || "—"}</p>

                                    <hr />

                                    <p><strong>Descrição:</strong></p>
                                    <p className="text-muted">{incidente.descricao || "—"}</p>

                                        {incidente.notas && (
                                            <>
                                            <hr />
                                            <p><strong>Notas:</strong></p>
                                            <p className="text-muted">{incidente.notas}</p>
                                            </>
                                        )}

                                    <hr />

                                    <p><strong>Data Criação:</strong> {formatarData(incidente.createdAt)}</p>
                                    <p><strong>Última Alteração:</strong> {formatarData(incidente.updatedAt)}</p>
                                    {incidente.dataConclusao && (
                                        <p><strong>Conclusão:</strong> {formatarData(incidente.dataConclusao)}</p>
                                    )}

                                    <div className="mt-3 d-flex justify-content-end">
                                        <Button
                                            variant="warning"
                                            size="sm"
                                            className="me-2"
                                            onClick={() => onEdit(incidente)}
                                        >
                                            Editar
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDeleteClick(incidente)}
                                        >
                                            Excluir
                                        </Button>
                                    </div>
                                </div>
                            </Accordion.Body>
                        </Accordion.Item>
                    ))}
                </Accordion>
            )}

            {/* Modal de confirmação */}
            <ConfirmModal
                show={showModal}
                onHide={handleCancelDelete}
                onConfirm={handleConfirmDelete}
                title="Confirmar exclusão"
                message={
                    incidenteSelecionado
                        ? `Deseja realmente excluir o incidente #${incidenteSelecionado.numeroChamado || "—"}?`
                        : "Deseja realmente excluir este incidente?"
                }
            />
        </div>
    );
};

// Função para formatar data de forma segura
const formatarData = (timestamp) => {
    if (!timestamp) return "—";
    const date =
        timestamp.toDate?.() instanceof Date
        ? timestamp.toDate()
        : new Date(timestamp);
    return date.toLocaleString("pt-BR");
};

export default IncidenteList;
