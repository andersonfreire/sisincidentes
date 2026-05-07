// Componente de Formulário para "Cadastrar" e "Atualizar" Usuários.

import React, { useState, useEffect } from "react";
import { Button, Form, Row, Col } from "react-bootstrap";
import { createUsuario, updateUsuario } from "../services/usuarioService";
import { getUnidades } from "../services/unidadeAdministrativaService";
import { initialUsuario } from "../models/usuarioModel";
import ToastMessage from "./ToastMessage/ToastMessage";

// Lista de opções de Função
const funcoes = [
    "Analista de TI",
    "Coordenador de SI",
    "Gestor de TI",
    "Superintendente de TI",
    "Técnico de TI",
];

const UsuarioForm = ({ selectedUsuario, setSelectedUsuario, onSave }) => {
    const [usuarioData, setUsuarioData] = useState(initialUsuario);
    const [unidades, setUnidades] = useState([]);
    const [loading, setLoading] = useState(false);

    // ✅ Estado do Toast
    const [toast, setToast] = useState({
        show: false,
        message: "",
        type: "success", // success | error | warning | info
    });

    const isEditMode = !!selectedUsuario;

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUsuarioData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    useEffect(() => {
        (async () => {
            try {
                const unidadesList = await getUnidades();
                setUnidades(unidadesList);
            } catch (error) {
                console.error("Erro ao carregar unidades: ", error);
                setToast({
                    show: true,
                    message: "Erro ao carregar unidades administrativas.",
                    type: "error",
                });
            }
        })();
    }, []);

    useEffect(() => {
        if (selectedUsuario) {
            setUsuarioData(selectedUsuario);
        } else {
            setUsuarioData(initialUsuario);
        }
    }, [selectedUsuario]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            if (isEditMode) {
                await updateUsuario(selectedUsuario.id, usuarioData);
                setToast({
                    show: true,
                    message: "Usuário atualizado com sucesso!",
                    type: "success",
                });
            } else {
                await createUsuario(usuarioData);
                setToast({
                    show: true,
                    message: "Usuário criado com sucesso!",
                    type: "success",
                });
            }

            handleCancel();
            if (onSave) onSave();
        } catch (error) {
            setToast({
                show: true,
                message: `Erro: ${error.message || "Não foi possível salvar o usuário."}`,
                type: "error",
            });
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        setUsuarioData(initialUsuario);
        setSelectedUsuario(null);
    };

    return (
        <div className="mb-4">
            <h4>{isEditMode ? "Editar Usuário" : "Novo Usuário"}</h4>

            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={isEditMode ? 12 : 8}>
                        <Form.Group className="mb-3">
                            <Form.Label>E-mail</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                placeholder="E-mail institucional"
                                value={usuarioData.email}
                                onChange={handleChange}
                                required
                                disabled={isEditMode}
                            />
                        </Form.Group>
                    </Col>

                    {!isEditMode && (
                        <Col md={4}>
                            <Form.Group className="mb-3">
                                <Form.Label>Senha</Form.Label>
                                <Form.Control
                                    type="password"
                                    name="senha"
                                    placeholder="Senha de acesso"
                                    value={usuarioData.senha}
                                    onChange={handleChange}
                                    required
                                />
                            </Form.Group>
                        </Col>
                    )}
                </Row>

                <Row>
                    <Col md={8}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nome Completo</Form.Label>
                            <Form.Control
                                type="text"
                                name="nome"
                                placeholder="Nome completo do usuário"
                                value={usuarioData.nome}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Matrícula</Form.Label>
                            <Form.Control
                                type="text"
                                name="matricula"
                                placeholder="Matrícula funcional"
                                value={usuarioData.matricula}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={5}>
                        <Form.Group className="mb-3">
                            <Form.Label>Unidade Administrativa</Form.Label>
                            <Form.Select
                                name="unidadeId"
                                value={usuarioData.unidadeId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">-- Selecione uma unidade --</option>
                                {unidades.map((u) => (
                                    <option key={u.id} value={u.id}>
                                        {u.titulo} ({u.sigla})
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Função</Form.Label>
                            <Form.Select
                                name="funcao"
                                value={usuarioData.funcao}
                                onChange={handleChange}
                                required
                            >
                                <option value="">-- Selecione uma função --</option>
                                {funcoes.map((f) => (
                                    <option key={f} value={f}>
                                        {f}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Telefone</Form.Label>
                            <Form.Control
                                type="text"
                                name="telefone"
                                placeholder="Contato"
                                value={usuarioData.telefone}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Observações</Form.Label>
                    <Form.Control
                        as="textarea"
                        rows={3}
                        name="observacoes"
                        placeholder="Anotações adicionais"
                        value={usuarioData.observacoes}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Button type="submit" variant="primary" disabled={loading} className="me-2">
                    {isEditMode ? "Atualizar" : "Adicionar"}
                </Button>
                <Button variant="danger" onClick={handleCancel}>
                    Cancelar
                </Button>
            </Form>

            {/* ✅ Componente de Toast */}
            <ToastMessage
                show={toast.show}
                message={toast.message}
                type={toast.type}
                onClose={() => setToast({ ...toast, show: false })}
            />
        </div>
    );
};

export default UsuarioForm;
