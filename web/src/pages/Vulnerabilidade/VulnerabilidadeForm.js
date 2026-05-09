import React, { useState, useEffect } from "react";
import { Button, Form, Row, Col } from "react-bootstrap";
import { createVulnerabilidade, updateVulnerabilidade } from "../../services/vulnerabilidadeService";

const initialData = { titulo: "", descricao: "", severidade: "MEDIA" };

const VulnerabilidadeForm = ({ selectedVulnerabilidade, onSaveSuccess, onCancel }) => {
    const [formData, setFormData] = useState(initialData);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (selectedVulnerabilidade) {
            setFormData({
                titulo: selectedVulnerabilidade.titulo || "",
                descricao: selectedVulnerabilidade.descricao || "",
                severidade: selectedVulnerabilidade.severidade || "MEDIA"
            });
        } else {
            setFormData(initialData);
        }
    }, [selectedVulnerabilidade]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (selectedVulnerabilidade) {
                await updateVulnerabilidade(selectedVulnerabilidade.id, formData);
            } else {
                await createVulnerabilidade(formData);
            }
            setFormData(initialData);
            if (onSaveSuccess) onSaveSuccess();
        } catch (error) {
            console.error("Erro na operação de vulnerabilidade:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-3 border border-dark rounded-0 bg-light">
            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={8}>
                        <Form.Group className="mb-3">
                            <Form.Label>Título da Vulnerabilidade</Form.Label>
                            <Form.Control 
                                className="rounded-0 shadow-none border-dark" 
                                type="text" 
                                name="titulo" 
                                value={formData.titulo} 
                                onChange={handleChange} 
                                required 
                            />
                        </Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Severidade</Form.Label>
                            <Form.Select 
                                className="rounded-0 shadow-none border-dark" 
                                name="severidade" 
                                value={formData.severidade} 
                                onChange={handleChange}
                            >
                                <option value="BAIXA">Baixa</option>
                                <option value="MEDIA">Média</option>
                                <option value="ALTA">Alta</option>
                                <option value="CRITICA">Crítica</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Descrição</Form.Label>
                    <Form.Control 
                        className="rounded-0 shadow-none border-dark" 
                        as="textarea" 
                        rows={3} 
                        name="descricao" 
                        value={formData.descricao} 
                        onChange={handleChange} 
                    />
                </Form.Group>

                <Button type="submit" variant="dark" className="rounded-0 shadow-none" disabled={loading}>
                    {selectedVulnerabilidade ? "Atualizar Registo" : "Registar Vulnerabilidade"}
                </Button>
                
                {selectedVulnerabilidade && (
                    <Button variant="outline-dark" className="rounded-0 shadow-none ms-2" onClick={onCancel}>
                        Cancelar Edição
                    </Button>
                )}
            </Form>
        </div>
    );
};

export default VulnerabilidadeForm;