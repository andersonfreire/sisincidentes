// Componente de Lista para Consultar (listar) e Excluir Usuários.

import React, { useEffect, useState } from "react";
import { Button, Table, Modal } from "react-bootstrap";
import { deleteUsuario, getUsuarios } from "../services/usuarioService";
import ToastMessage from "./ToastMessage/ToastMessage";

const UsuarioList = ({ onEdit }) => {
    const [usuarios, setUsuarios] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [usuarioToDelete, setUsuarioToDelete] = useState(null);

    // ✅ Estado do Toast
    const [toast, setToast] = useState({
        show: false,
        message: "",
        type: "success", // success | error | info
    });

    // Consultar (Listar)
    const fetchUsuarios = async () => {
        try {
            const response = await getUsuarios();
            setUsuarios(response);
        } catch (error) {
            console.error("Erro ao buscar usuários:", error);
            setToast({
                show: true,
                message: "Erro ao carregar lista de usuários.",
                type: "error",
            });
        }
    };

    useEffect(() => {
        fetchUsuarios();
    }, []);

    // Abrir modal de confirmação
    const handleShowModal = (usuario) => {
        setUsuarioToDelete(usuario);
        setShowModal(true);
    };

    // Fechar modal
    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioToDelete(null);
    };

    // Confirmar exclusão
    const handleConfirmDelete = async () => {
        if (!usuarioToDelete) return;

        try {
            await deleteUsuario(usuarioToDelete.id);
            setToast({
                show: true,
                message: `Usuário "${usuarioToDelete.nome}" excluído com sucesso.`,
                type: "success",
            });
            fetchUsuarios();
        } catch (error) {
            console.error("Erro ao excluir usuário:", error);
            setToast({
                show: true,
                message: "Erro ao excluir o usuário.",
                type: "error",
            });
        } finally {
            handleCloseModal();
        }
    };

    return (
        <div>
            <h4 className="mb-3">Usuários Cadastrados</h4>

            <Table striped bordered hover responsive>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Matrícula</th>
                        <th>Função</th>
                        <th style={{ width: "160px" }}>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {usuarios.length > 0 ? (
                        usuarios.map((user) => (
                            <tr key={user.id}>
                                <td>{user.nome}</td>
                                <td>{user.email}</td>
                                <td>{user.matricula || "-"}</td>
                                <td>{user.funcao}</td>
                                <td>
                                    <Button
                                        variant="warning"
                                        size="sm"
                                        className="me-2"
                                        onClick={() => onEdit(user)}
                                    >
                                        Editar
                                    </Button>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleShowModal(user)}
                                    >
                                        Excluir
                                    </Button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="text-center">
                                Nenhum usuário cadastrado.
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
                    {usuarioToDelete ? (
                        <>
                            <p>
                                Tem certeza que deseja <strong>excluir</strong> o
                                usuário:
                            </p>
                            <p className="mb-0">
                                <strong>{usuarioToDelete.nome}</strong> <br />
                                <small>{usuarioToDelete.email}</small>
                            </p>
                            <hr />
                            <p className="text-danger small mb-0">
                                ⚠️ Isso removerá o registro do Firestore, mas não
                                afetará o usuário no sistema de autenticação.
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

export default UsuarioList;
