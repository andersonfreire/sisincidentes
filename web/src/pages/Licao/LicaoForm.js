import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import { createLicao, updateLicao } from "../../services/licaoService";
import { getIncidentes } from "../../services/incidenteService";
import ToastMessage from "../../components/ToastMessage/ToastMessage";

const LicaoForm = ({ selectedLesson, onSave }) => {
    const [formData, setFormData] = useState({ incidenteId: "", descricaoResolucao: "" });
    const [incidentes, setIncidentes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [toast, setToast] = useState({ show: false, message: "", variant: "success" });

    useEffect(() => {
        const fetchIncidentes = async () => {
            try {
                const data = await getIncidentes();
                setIncidentes(Array.isArray(data) ? data : []);
            } catch (error) {
                setToast({ show: true, message: "Erro ao carregar incidentes.", variant: "danger" });
            }
        };
        fetchIncidentes();
    }, []);

    useEffect(() => {
        if (selectedLesson) {
            setFormData({
                incidenteId: selectedLesson.incidenteId || (selectedLesson.incidente && selectedLesson.incidente.id) || "",
                descricaoResolucao: selectedLesson.descricaoResolucao || ""
            });
        } else {
            setFormData({ incidenteId: "", descricaoResolucao: "" });
        }
    }, [selectedLesson]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (selectedLesson) await updateLicao(selectedLesson.id, formData);
            else await createLicao(formData);
            
            setFormData({ incidenteId: "", descricaoResolucao: "" });
            onSave();
            setToast({ show: true, message: "Operação realizada com sucesso.", variant: "success" });
        } catch (error) {
            setToast({ show: true, message: "Falha na operação. Verifique se o incidente já possui lição registada (1:1).", variant: "danger" });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-3 border border-dark rounded-0 bg-light">
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>Incidente Relacionado</Form.Label>
                    <Form.Select
                        className="rounded-0 shadow-none border-dark"
                        name="incidenteId"
                        value={formData.incidenteId}
                        onChange={handleChange}
                        required
                        disabled={!!selectedLesson} // Relação 1:1, a edição não deve alterar a chave estrangeira
                    >
                        <option value="">Selecione o Incidente...</option>
                        {incidentes.map(inc => (
                            <option key={inc.id} value={inc.id}>
                                #{inc.numeroChamado || inc.id} — {inc.titulo || "Sem Título"}
                            </option>
                        ))}
                    </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Descrição da Resolução (Lição Aprendida)</Form.Label>
                    <Form.Control
                        className="rounded-0 shadow-none border-dark"
                        as="textarea"
                        rows={5}
                        name="descricaoResolucao"
                        value={formData.descricaoResolucao}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <div className="mt-3">
                    <Button type="submit" variant="dark" className="rounded-0 shadow-none" disabled={loading}>
                        {selectedLesson ? "Atualizar Registo" : "Gravar Lição"}
                    </Button>
                    {selectedLesson && (
                        <Button variant="outline-dark" className="ms-2 rounded-0 shadow-none" onClick={() => { setFormData({ incidenteId: "", descricaoResolucao: "" }); onSave(); }}>
                            Cancelar
                        </Button>
                    )}
                </div>
            </Form>
            <ToastMessage show={toast.show} onClose={() => setToast({ ...toast, show: false })} message={toast.message} variant={toast.variant} />
        </div>
    );
};

export default LicaoForm;