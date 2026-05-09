import React, { useState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import { createVulnerabilidade, updateVulnerabilidade } from "../../services/vulnerabilidadeService";

const VulnerabilidadeForm = ({ selected, onSave, onCancel }) => {
    const [formData, setFormData] = useState({ titulo: "", descricao: "", severidade: "MEDIA" });

    useEffect(() => {
        if (selected) setFormData({ titulo: selected.titulo, descricao: selected.descricao, severidade: selected.severidade });
    }, [selected]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (selected) await updateVulnerabilidade(selected.id, formData);
        else await createVulnerabilidade(formData);
        onSave();
    };

    return (
        <Form onSubmit={handleSubmit} className="border p-3 rounded-0 bg-light">
            <Form.Group className="mb-3">
                <Form.Label>Título</Form.Label>
                <Form.Control className="rounded-0 shadow-none border-dark" type="text" value={formData.titulo} onChange={e => setFormData({...formData, titulo: e.target.value})} required />
            </Form.Group>
            <Form.Group className="mb-3">
                <Form.Label>Severidade</Form.Label>
                <Form.Select className="rounded-0 shadow-none border-dark" value={formData.severidade} onChange={e => setFormData({...formData, severidade: e.target.value})}>
                    <option value="BAIXA">Baixa</option>
                    <option value="MEDIA">Média</option>
                    <option value="ALTA">Alta</option>
                    <option value="CRITICA">Crítica</option>
                </Form.Select>
            </Form.Group>
            <Button type="submit" variant="dark" className="rounded-0 shadow-none">Salvar</Button>
            <Button variant="outline-dark" className="rounded-0 shadow-none ms-2" onClick={onCancel}>Cancelar</Button>
        </Form>
    );
};

export default VulnerabilidadeForm;