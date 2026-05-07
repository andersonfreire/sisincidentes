// Componente de Formulário para "Cadastrar" e "Atualizar" Unidades.

import React, { useState, useEffect } from "react";
import { Button, Form, Row, Col } from "react-bootstrap";
import { createUnidade, updateUnidade } from "../services/unidadeAdministrativaService";
import { initialUnidadeAdministrativa } from "../models/unidadeAdministrativaModel";
import ToastMessage from "./ToastMessage/ToastMessage";

const UnidadeAdministrativaForm = ({ selectedUnidade, setSelectedUnidade, onSave }) => {
    const [unidadeData, setUnidadeData] = useState(initialUnidadeAdministrativa);
    const [loading, setLoading] = useState(false);

    // ✅ Estado do Toast
    const [toast, setToast] = useState({
        show: false,
        message: "",
        type: "success", // success | error | info
    });

    // Handler genérico
    const handleChange = (e) => {
        const { name, value } = e.target;
        setUnidadeData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    // Monitora a seleção (modo edição)
    useEffect(() => {
        if (selectedUnidade) {
            setUnidadeData(selectedUnidade);
        } else {
            setUnidadeData(initialUnidadeAdministrativa);
        }
    }, [selectedUnidade]);

    // Submissão do formulário
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            if (selectedUnidade) {
                await updateUnidade(selectedUnidade.id, unidadeData);
                setToast({
                    show: true,
                    message: "Unidade atualizada com sucesso!",
                    type: "success",
                });
            } else {
                const res = await createUnidade(unidadeData);
                setToast({
                    show: true,
                    message: `Unidade criada com sucesso! ID: ${res.id}`,
                    type: "success",
                });
            }

            handleCancel();
            if (onSave) onSave();
        } catch (error) {
            console.error("Erro ao salvar unidade:", error);
            setToast({
                show: true,
                message: `Erro ao salvar unidade: ${error.message || "Tente novamente."}`,
                type: "error",
            });
        } finally {
            setLoading(false);
        }
    };

    // Reset do formulário
    const handleCancel = () => {
        setUnidadeData(initialUnidadeAdministrativa);
        setSelectedUnidade(null);
    };

    return (
        <div className="mb-4">
            <h4>{selectedUnidade ? "Editar Unidade" : "Nova Unidade Administrativa"}</h4>
            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Código</Form.Label>
                            <Form.Control
                                type="text"
                                name="codigo"
                                placeholder="Ex: 1.25.01"
                                value={unidadeData.codigo}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                    </Col>
                    <Col md={8}>
                        <Form.Group className="mb-3">
                            <Form.Label>Sigla</Form.Label>
                            <Form.Control
                                type="text"
                                name="sigla"
                                placeholder="Ex: STI"
                                value={unidadeData.sigla}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Título (Nome Completo)</Form.Label>
                    <Form.Control
                        type="text"
                        name="titulo"
                        placeholder="Ex: Superintendência de Tecnologia da Informação"
                        value={unidadeData.titulo}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Responsável</Form.Label>
                    <Form.Control
                        type="text"
                        name="responsavel"
                        placeholder="Nome do gestor ou responsável"
                        value={unidadeData.responsavel}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Contato</Form.Label>
                    <Form.Control
                        as="textarea"
                        rows={3}
                        name="contato"
                        placeholder="Telefone, e-mail ou ramal"
                        value={unidadeData.contato}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Button type="submit" variant="primary" disabled={loading} className="me-2">
                    {selectedUnidade ? "Atualizar" : "Adicionar"}
                </Button>
                <Button variant="danger" onClick={handleCancel}>
                    Cancelar
                </Button>
            </Form>

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

export default UnidadeAdministrativaForm;
