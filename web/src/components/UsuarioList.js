// Componente de Lista para Consultar (listar), Excluir e Ativar/Desativar Usuários.

import React, { useEffect, useState } from "react";
import { Button, Table, Modal, Badge } from "react-bootstrap";
import { deleteUsuario, getUsuarios, toggleAtivoUsuario } from "../services/usuarioService";
import ToastMessage from "./ToastMessage/ToastMessage";

const UsuarioList = ({ onEdit }) => {
    const [usuarios, setUsuarios] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [usuarioToDelete, setUsuarioToDelete] = useState(null);

    // Estado do Toast
    const [toast, setToast] = useState({
        show: false,
        message: "",
        type: "success", 
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

    // Alternar status do usuário (Ativo/Inativo)
    const handleToggleAtivo = async (usuario) => {
        try {
            const result = await toggleAtivoUsuario(usuario.id);
            setToast({
                show: true,
                message: result.message,
                type: "success",
            });
            fetchUsuarios(); // Recarrega a lista
        } catch (error) {
            console.error("Erro ao alternar status do usuário:", error);
            setToast({
                show: true,
                message: error.message || "Erro ao alterar o status do usuário.",
                type: "error",
            });
        }
    };

    // Abrir modal de confirmação de exclusão
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
                message: `Usuário "${usuarioToDelete.nome}" excluído permanentemente.`,
                type: "success",
            });
            fetchUsuarios();
        } catch (error) {
            console.error("Erro ao excluir usuário:", error);
            setToast({
                show: true,
                message: error.message || "Erro ao excluir o usuário.",
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
                        <th>Unidade</th>
                        <th>Função</th>
                        <th>Perfil</th>
                        <th>Status</th>
                        <th style={{ width: "240px", textAlign: "center" }}>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {usuarios && usuarios.length > 0 ? (
                        usuarios.map((user) => (
                            <tr key={user.id}>
                                <td className="align-middle">{user.nome}</td>
                                <td className="align-middle">{user.email}</td>
                                <td className="align-middle">{user.unidadeSigla || "-"}</td>
                                <td className="align-middle">{user.funcaoDescricao || user.funcao}</td>
                                <td className="align-middle">
                                    <Badge bg={user.perfil === 'ADMIN' ? 'dark' : 'secondary'}>
                                        {user.perfil}
                                    </Badge>
                                </td>
                                <td className="align-middle text-center">
                                    <Badge bg={user.ativo ? 'success' : 'danger'}>
                                        {user.ativo ? 'Ativo' : 'Inativo'}
                                    </Badge>
                                </td>
                                <td className="align-middle text-center">
                                    <Button
                                        variant="warning"
                                        size="sm"
                                        className="me-2 mb-1"
                                        onClick={() => onEdit(user)}
                                    >
                                        Editar
                                    </Button>
                                    <Button
                                        variant={user.ativo ? "secondary" : "success"}
                                        size="sm"
                                        className="me-2 mb-1"
                                        onClick={() => handleToggleAtivo(user)}
                                    >
                                        {user.ativo ? "Desativar" : "Ativar"}
                                    </Button>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        className="mb-1"
                                        onClick={() => handleShowModal(user)}
                                    >
                                        Excluir
                                    </Button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="7" className="text-center">
                                Nenhum usuário cadastrado.
                            </td>
                        </tr>
                    )}
                </tbody>
            </Table>

            {/* Modal de confirmação */}
            <Modal show={showModal} onHide={handleCloseModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmar exclusão permanente</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {usuarioToDelete ? (
                        <>
                            <p>
                                Tem certeza que deseja <strong>excluir permanentemente</strong> o
                                usuário:
                            </p>
                            <p className="mb-0">
                                <strong>{usuarioToDelete.nome}</strong> <br />
                                <small>{usuarioToDelete.email}</small>
                            </p>
                            <hr />
                            <p className="text-danger small mb-0">
                                ⚠️ Atenção: Esta ação não pode ser desfeita. Se o usuário já tiver registros (como incidentes), a exclusão pode falhar. Nesses casos, prefira apenas <strong>Desativar</strong> o usuário.
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
                        Excluir Permanentemente
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Toast de mensagens */}
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
