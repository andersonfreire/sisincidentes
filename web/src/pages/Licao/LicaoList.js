import React, { useEffect, useState } from "react";
import { Accordion, Button, Spinner } from "react-bootstrap";
import { getLicoes, deleteLicao } from "../../services/licaoService";
import { getIncidenteById } from "../../services/incidenteService";
import ConfirmModal from "../../components/ConfirmModal/ConfirmModal";

const LicaoList = ({ onEdit, refresh }) => {
    const [licoes, setLicoes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [licaoSelecionada, setLicaoSelecionada] = useState(null);

    const fetchLicoes = async () => {
        setLoading(true);
        try {
            const data = await getLicoes();
            const validData = Array.isArray(data) ? data : [];
            
            const enriquecido = await Promise.all(validData.map(async (licao) => {
                const idInc = licao.incidenteId || (licao.incidente && licao.incidente.id);
                let numeroChamado = licao.incidente ? licao.incidente.numeroChamado : "—";
                
                if (!licao.incidente && idInc) {
                    try {
                        const incData = await getIncidenteById(idInc);
                        numeroChamado = incData.numeroChamado || incData.id;
                    } catch (e) {
                        numeroChamado = idInc;
                    }
                }
                return { ...licao, numeroChamado };
            }));
            
            setLicoes(enriquecido);
        } catch (error) {
            console.error("Erro na extração de dados:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchLicoes(); }, [refresh]);

    const handleConfirmDelete = async () => {
        if (licaoSelecionada) {
            await deleteLicao(licaoSelecionada.id);
            setShowModal(false);
            setLicaoSelecionada(null);
            fetchLicoes();
        }
    };

    if (loading) return <div className="text-center p-4"><Spinner animation="border" variant="dark" /></div>;

    return (
        <div className="mt-3">
            {licoes.length === 0 ? (
                <p className="text-muted border border-dark p-3 bg-light rounded-0">Nenhum registo consolidado.</p>
            ) : (
                <Accordion className="border-0 gap-2 d-flex flex-column">
                    {licoes.map(licao => (
                        <Accordion.Item key={licao.id} eventKey={licao.id.toString()} className="border border-dark rounded-0 shadow-none bg-light">
                            <Accordion.Header className="shadow-none">
                                <strong>Lição #{licao.id} — Incidente Relacionado #{licao.numeroChamado}</strong>
                            </Accordion.Header>
                            <Accordion.Body className="bg-white border-top border-dark rounded-0">
                                <p><strong>Descrição da Resolução:</strong></p>
                                <p className="text-justify">{licao.descricaoResolucao}</p>
                                
                                <div className="d-flex justify-content-end mt-4">
                                    <Button variant="outline-dark" size="sm" className="rounded-0 shadow-none me-2" onClick={() => onEdit(licao)}>Editar</Button>
                                    <Button variant="dark" size="sm" className="rounded-0 shadow-none" onClick={() => { setLicaoSelecionada(licao); setShowModal(true); }}>Eliminar</Button>
                                </div>
                            </Accordion.Body>
                        </Accordion.Item>
                    ))}
                </Accordion>
            )}

            <ConfirmModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onConfirm={handleConfirmDelete}
                title="Confirmação de Exclusão"
                message="Proceder com a eliminação do registo de lição aprendida?"
            />
        </div>
    );
};

export default LicaoList;