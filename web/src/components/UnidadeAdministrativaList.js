// Componente de Lista para "Consultar" (listar) e "Excluir" Unidades.

import React, { useEffect, useState } from "react";
import { Button, Table, Modal } from "react-bootstrap";
import { deleteUnidade, getUnidades } from "../services/unidadeAdministrativaService";
import ToastMessage from "../components/ToastMessage/ToastMessage"; // ✅ Importa o Toast

const UnidadeAdministrativaList = ({ onEdit }) => {
    const [unidades, setUnidades] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [unidadeToDelete, setUnidadeToDelete] = useState(null);

    // ✅ Estado do Toast
    const [toast, setToast] = useState({
        show: false,
        message: "",
        type: "success", // success | error | info
    });

    // Consultar (Listar)
    const fetchUnidades = async () => {
        try {
            const response = await getUnidades();
            setUnidades(response);
        } catch (error) {
            console.error("Erro ao buscar unidades:", error);
            setToast({
                show: true,
                message: "Erro ao carregar lista de unidades.",
                type: "error",
            });
        }
    };

    useEffect(() => {
        fetchUnidades();
    }, []);

    // Abrir modal de confirmação
    const handleShowModal = (unidade) => {
        setUnidadeToDelete(unidade);
        setShowModal(true);
    };

    // Fechar modal
    const handleCloseModal = () => {
        setShowModal(false);
        setUnidadeToDelete(null);
    };

    // Confirmar exclusão
    const handleConfirmDelete = async () => {
        if (!unidadeToDelete) return;

        try {
            await deleteUnidade(unidadeToDelete.id);
            setToast({
                show: true,
                message: `Unidade "${unidadeToDelete.titulo}" excluída com sucesso.`,
                type: "success",
            });
            fetchUnidades(); // Atualiza lista
        } catch (error) {
            console.error("Erro ao excluir unidade:", error);
            setToast({
                show: true,
                message: "Erro ao excluir unidade. Tente novamente.",
                type: "error",
            });
        } finally {
            handleCloseModal();
        }
    };

    return (
        <div>
            <h4 className="mb-3">Unidades Administrativas</h4>

            <Table striped bordered hover responsive>
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Título</th>
                        <th>Sigla</th>
                        <th>Responsável</th>
                        <th>Contato</th>
                        <th style={{ width: "160px" }}>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {unidades.length > 0 ? (
                        unidades.map((ua) => (
                            <tr key={ua.id}>
                                <td>{ua.codigo}</td>
                                <td>{ua.titulo}</td>
                                <td>{ua.sigla}</td>
                                <td>{ua.responsavel || "-"}</td>
                                <td>{ua.contato || "-"}</td>
                                <td>
                                    <Button
                                        variant="warning"
                                        size="sm"
                                        className="me-2"
                                        onClick={() => onEdit(ua)}
                                    >
                                        Editar
                                    </Button>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleShowModal(ua)}
                                    >
                                        Excluir
                                    </Button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6" className="text-center">
                                Nenhuma unidade cadastrada.
                            </td>
                        </tr>
                    )}
                </tbody>
            </Table>

            {/* ✅ Modal de confirmação */}
            <Modal show={showModal} onHide={handleCloseModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmar exclusão</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {unidadeToDelete ? (
                        <>
                            <p>Tem certeza que deseja excluir a unidade:</p>
                            <p className="mb-0">
                                <strong>{unidadeToDelete.titulo}</strong> <br />
                                <small>
                                    Código: {unidadeToDelete.codigo} — Sigla:{" "}
                                    {unidadeToDelete.sigla}
                                </small>
                            </p>
                            <hr />
                            <p className="text-danger small mb-0">
                                ⚠️ Esta ação é irreversível e removerá a unidade
                                do banco de dados.
                            </p>
                        </>
                    ) : (
                        "Carregando..."
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Cancelar
                    </Button>
                    <Button variant="danger" onClick={handleConfirmDelete}>
                        Excluir
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* ✅ Toast de mensagens */}
            <ToastMessage
                show={toast.show}
                message={toast.message}
                type={toast.type}
                onClose={() => setToast({ ...toast, show: false })}
            />
        </div>
    );
};

export default UnidadeAdministrativaList;
