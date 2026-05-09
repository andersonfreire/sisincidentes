import React, { useState, useEffect } from "react";
import { Button, Form, Row, Col } from "react-bootstrap";
import { getCategories } from "../../services/categoriaService";
import { createIncidente, updateIncidente } from "../../services/incidenteService";
import { getUsuarios } from "../../services/usuarioService";
import { getUnidades } from "../../services/unidadeAdministrativaService";
import { getVulnerabilidades } from "../../services/vulnerabilidadeService";

const initialFormData = {
    titulo: "", descricao: "", status: "ABERTO", numeroChamado: "",
    tarefaRelacionada: "", tipo: "", prioridade: "", ipOrigem: "",
    ipDestino: "", host: "", tempoEstimado: "", cc: "", notas: "",
    categoriaId: "", unidadeId: "", autorId: "", atribuidoId: "",
    vulnerabilidadesIds: []
};

const IncidenteForm = ({ selectedIncidente, setSelectedIncidente, onSave }) => {
    const [formData, setFormData] = useState(initialFormData);
    const [loading, setLoading] = useState(false);
    const [listas, setListas] = useState({ categorias: [], usuarios: [], unidades: [], vulnerabilidades: [] });

    useEffect(() => {
        Promise.all([getCategories(), getUsuarios(), getUnidades(), getVulnerabilidades()])
            .then(([cat, usr, uni, vul]) => setListas({ categorias: cat || [], usuarios: usr || [], unidades: uni || [], vulnerabilidades: vul || [] }))
            .catch(console.error);
    }, []);

    useEffect(() => {
        if (selectedIncidente) {
            setFormData({
                ...initialFormData,
                ...selectedIncidente,
                vulnerabilidadesIds: selectedIncidente.vulnerabilidades ? selectedIncidente.vulnerabilidades.map(v => v.id) : []
            });
        } else {
            setFormData(initialFormData);
        }
    }, [selectedIncidente]);

    const handleChange = (e) => {
        const { name, value, options } = e.target;
        if (name === "vulnerabilidadesIds") {
            setFormData(prev => ({ ...prev, [name]: Array.from(options).filter(o => o.selected).map(o => o.value) }));
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (selectedIncidente) await updateIncidente(selectedIncidente.id, formData);
            else await createIncidente(formData);
            
            setFormData(initialFormData);
            setSelectedIncidente(null);
            if (onSave) onSave();
        } catch (error) {
            console.error("Erro ao salvar:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-3 border border-dark rounded-0 bg-light">
            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={12}>
                        <Form.Group className="mb-3">
                            <Form.Label>Título</Form.Label>
                            <Form.Control className="rounded-0 shadow-none border-dark" type="text" name="titulo" value={formData.titulo} onChange={handleChange} required />
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col md={4}>
                        <Form.Group className="mb-3"><Form.Label>Nº Chamado</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="numeroChamado" value={formData.numeroChamado} onChange={handleChange} /></Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3"><Form.Label>Tarefa Relacionada</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="tarefaRelacionada" value={formData.tarefaRelacionada} onChange={handleChange} /></Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tipo</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="tipo" value={formData.tipo} onChange={handleChange}>
                                <option value="">Selecione...</option><option value="INCIDENTE">Incidente</option><option value="VULNERABILIDADE">Vulnerabilidade</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Status</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="status" value={formData.status} onChange={handleChange} required>
                                <option value="ABERTO">Aberto</option><option value="EM_ANDAMENTO">Em Andamento</option><option value="CONCLUIDO">Concluído</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Prioridade</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="prioridade" value={formData.prioridade} onChange={handleChange}>
                                <option value="">Selecione...</option><option value="BAIXA">Baixa</option><option value="MEDIA">Média</option><option value="ALTA">Alta</option><option value="CRITICA">Crítica</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Categoria</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="categoriaId" value={formData.categoriaId} onChange={handleChange}>
                                <option value="">Selecione...</option>{listas.categorias.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Unidade</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="unidadeId" value={formData.unidadeId} onChange={handleChange}>
                                <option value="">Selecione...</option>{listas.unidades.map(u => <option key={u.id} value={u.id}>{u.sigla}</option>)}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Autor</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="autorId" value={formData.autorId} onChange={handleChange}>
                                <option value="">Selecione...</option>{listas.usuarios.map(u => <option key={u.id} value={u.id}>{u.nome}</option>)}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Atribuído para</Form.Label>
                            <Form.Select className="rounded-0 shadow-none border-dark" name="atribuidoId" value={formData.atribuidoId} onChange={handleChange}>
                                <option value="">Selecione...</option>{listas.usuarios.map(u => <option key={u.id} value={u.id}>{u.nome}</option>)}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col md={4}><Form.Group className="mb-3"><Form.Label>IP Origem</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="ipOrigem" value={formData.ipOrigem} onChange={handleChange} /></Form.Group></Col>
                    <Col md={4}><Form.Group className="mb-3"><Form.Label>IP Destino</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="ipDestino" value={formData.ipDestino} onChange={handleChange} /></Form.Group></Col>
                    <Col md={4}><Form.Group className="mb-3"><Form.Label>Host</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="host" value={formData.host} onChange={handleChange} /></Form.Group></Col>
                </Row>
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Tempo Estimado</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="tempoEstimado" value={formData.tempoEstimado} onChange={handleChange} /></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>CC</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" type="text" name="cc" value={formData.cc} onChange={handleChange} /></Form.Group></Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Vulnerabilidades Relacionadas</Form.Label>
                    <Form.Select className="rounded-0 shadow-none border-dark" multiple name="vulnerabilidadesIds" value={formData.vulnerabilidadesIds} onChange={handleChange}>
                        {listas.vulnerabilidades.map(v => <option key={v.id} value={v.id}>{v.titulo}</option>)}
                    </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3"><Form.Label>Descrição</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" as="textarea" rows={3} name="descricao" value={formData.descricao} onChange={handleChange} required /></Form.Group>
                <Form.Group className="mb-3"><Form.Label>Notas</Form.Label><Form.Control className="rounded-0 shadow-none border-dark" as="textarea" rows={3} name="notas" value={formData.notas} onChange={handleChange} /></Form.Group>

                <div className="mt-3">
                    <Button type="submit" variant="dark" className="rounded-0 shadow-none" disabled={loading}>{selectedIncidente ? "Atualizar" : "Registrar"}</Button>
                    <Button variant="outline-dark" className="rounded-0 shadow-none ms-2" onClick={() => { setFormData(initialFormData); setSelectedIncidente(null); }}>Cancelar</Button>
                </div>
            </Form>
        </div>
    );
};

export default IncidenteForm;