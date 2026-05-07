import React, { useState, useEffect } from "react";
import { Button, Form, Row, Col } from "react-bootstrap";
import { initialIncidenteVulnerabilidade } from "../../models/incidenteVulnerabilidadeModel";
import { getCategories } from "../../services/categoryService";
import { createIncidente, updateIncidente } from "../../services/incidentService";
import { getUsuarios } from "../../services/usuarioService";
import { getUnidades } from "../../services/unidadeAdministrativaService";
import ToastMessage from "../../components/ToastMessage/ToastMessage";

const IncidenteForm = ({ selectedIncidente, setSelectedIncidente, onSave }) => {
    const [formData, setFormData] = useState(initialIncidenteVulnerabilidade);
    const [loading, setLoading] = useState(false);
    const [categorias, setCategorias] = useState([]);
    const [usuarios, setUsuarios] = useState([]);
    const [unidades, setUnidades] = useState([]);


    const [toast, setToast] = useState({ show: false, message: "", variant: "success" });

    const showToast = (message, variant = "success") => {
        setToast({ show: true, message, variant });
    };

    const hideToast = () => {
        setToast({ ...toast, show: false });
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [cats, users, units] = await Promise.all([
                    getCategories(),
                    getUsuarios(),
                    getUnidades()
                ]);
                setCategorias(cats);
                setUsuarios(users);
                setUnidades(units);
            } catch (error) {
                console.error("Erro ao carregar listas:", error);
                showToast("Erro ao carregar listas de dados.", "danger");
            }
        };
        fetchData();
    }, []);

    useEffect(() => {
        if (selectedIncidente) {
            setFormData(selectedIncidente);
        } else {
            setFormData(initialIncidenteVulnerabilidade);
        }
    }, [selectedIncidente]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (selectedIncidente) {
                await updateIncidente(selectedIncidente.id, formData);
                showToast("Incidente atualizado com sucesso!");
            } else {
                const res = await createIncidente(formData);
                showToast(`Incidente criado com sucesso! ID: ${res.id}`);
            }
            setFormData(initialIncidenteVulnerabilidade);
            setSelectedIncidente(null);
            if (onSave) onSave();
        } catch (error) {
            showToast(`Erro: ${error.message}`, "danger");
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        setFormData(initialIncidenteVulnerabilidade);
        setSelectedIncidente(null);
    };

    return (
        <div className="mb-4 py-3">
            <h4 className="mb-4">{selectedIncidente ? "Editar Incidente/Vulnerabildiade" : "Registrar Incidente/Vulnerabilidade"}</h4>
            <Form onSubmit={handleSubmit}>
                {/* === Identificação básica === */}

                <Row>
                    <Col md={12}>
                        <Form.Group className="mb-3">
                            <Form.Label>Assunto</Form.Label>
                            <Form.Control
                                type="text"
                                name="assunto"
                                value={formData.assunto}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>   
                    </Col>
                </Row>
                
                <Row>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Número do Chamado</Form.Label>
                            <Form.Control
                                type="text"
                                name="numeroChamado"
                                value={formData.numeroChamado}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tarefa Relacionada</Form.Label>
                            <Form.Control
                                type="text"
                                name="tarefaRelacionada"
                                value={formData.tarefaRelacionada}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tipo</Form.Label>
                            <Form.Select
                                name="tipo"
                                value={formData.tipo}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Selecione...</option>
                                <option value="Incidente">Incidente</option>
                                <option value="Vulnerabilidade">Vulnerabilidade</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>

                {/* === Assunto, Categoria e Unidade === */}
                <Row>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Situação</Form.Label>
                            <Form.Select
                                name="situacao"
                                value={formData.situacao}
                                onChange={handleChange}
                                required
                            >
                                <option value="Aberta">Aberta</option>
                                <option value="Em Andamento">Em Andamento</option>
                                <option value="Concluída">Concluída</option>
                                <option value="Cancelada">Cancelada</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Prioridade</Form.Label>
                            <Form.Select
                                name="prioridade"
                                value={formData.prioridade}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Selecione...</option>
                                <option value="Baixa">Baixa</option>
                                <option value="Média">Média</option>
                                <option value="Alta">Alta</option>
                                <option value="Crítica">Crítica</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Categoria</Form.Label>
                            <Form.Select
                                name="categoriaId"
                                value={formData.categoriaId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Selecione...</option>
                                {categorias.map((cat) => (
                                    <option key={cat.id} value={cat.id}>
                                        {cat.nome}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Unidade Administrativa</Form.Label>
                            <Form.Select
                                name="unidadeId"
                                value={formData.unidadeId}
                                onChange={handleChange}
                            >
                                <option value="">Selecione...</option>
                                {unidades.map((u) => (
                                    <option key={u.id} value={u.id}>
                                        {u.sigla}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>

                {/* === Responsáveis === */}
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Autor</Form.Label>
                            <Form.Select
                                name="autorId"
                                value={formData.autorId}
                                onChange={handleChange}
                            >
                                <option value="">Selecione...</option>
                                {usuarios.map((u) => (
                                    <option key={u.id} value={u.id}>
                                        {u.nome}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Atribuído para</Form.Label>
                            <Form.Select
                                name="atribuidoId"
                                value={formData.atribuidoId}
                                onChange={handleChange}
                            >
                                <option value="">Selecione...</option>
                                {usuarios.map((u) => (
                                    <option key={u.id} value={u.id}>
                                        {u.nome}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                </Row>

                {/* === Detalhes técnicos === */}
                <Row>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>IP de Origem</Form.Label>
                            <Form.Control
                                type="text"
                                name="ipOrigem"
                                value={formData.ipOrigem}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>IP de Destino</Form.Label>
                            <Form.Control
                                type="text"
                                name="ipDestino"
                                value={formData.ipDestino}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nome do Host</Form.Label>
                            <Form.Control
                                type="text"
                                name="host"
                                value={formData.host}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>
                </Row>                

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tempo estimado para resolução</Form.Label>
                            <Form.Control
                                type="text"
                                name="tempoEstimado"
                                value={formData.tempoEstimado}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>CC (emails notificados)</Form.Label>
                            <Form.Control
                                type="text"
                                name="cc"
                                value={formData.cc}
                                onChange={handleChange}
                                placeholder="ex: usuario1@ufrn.br, usuario2@ufrn.br"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Descrição</Form.Label>
                    <Form.Control
                        as="textarea"
                        rows={3}
                        name="descricao"
                        value={formData.descricao}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Notas</Form.Label>
                    <Form.Control
                        as="textarea"
                        rows={3}
                        name="notas"
                        value={formData.notas}
                        onChange={handleChange}
                    />
                </Form.Group>

                <div className="mt-3">
                    <Button type="submit" variant="primary" disabled={loading}>
                        {selectedIncidente ? "Atualizar" : "Adicionar"}
                    </Button>
                    <Button variant="secondary" className="ms-2" onClick={handleCancel}>
                        Cancelar
                    </Button>
                </div>
            </Form>

            <ToastMessage
                show={toast.show}
                onClose={hideToast}
                message={toast.message}
                variant={toast.variant}
            />
        </div>
    );
};

export default IncidenteForm;
