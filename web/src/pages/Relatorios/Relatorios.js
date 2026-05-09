import React, { useState, useEffect } from "react";
import { getRelatoriosData } from "../../services/relatoriosService";
import { Form, Row, Col, Card } from "react-bootstrap";
import { PieChart, Pie, Cell, Tooltip, Legend, BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer } from 'recharts';
import "./Relatorios.css";

const Relatorios = () => {
    const [period, setPeriod] = useState("last12months");
    const [stats, setStats] = useState({
        openIncidents: 0,
        openVulnerabilities: 0,
        ongoingIncidents: 0,
        ongoingVulnerabilities: 0,
        completedIncidentsPercentage: 0,
        completedVulnerabilitiesPercentage: 0,
    });
    const [categoryData, setCategoryData] = useState([]);
    const [unitData, setUnitData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const now = new Date();
            let startDate, endDate;

            if (period === "last12months") {
                startDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());
                endDate = now;
            } else {
                const year = parseInt(period, 10);
                startDate = new Date(year, 0, 1);
                endDate = new Date(year, 11, 31);
            }

            const data = await getRelatoriosData(startDate, endDate);
            setStats(data.stats);
            setCategoryData(data.categoryData);
            setUnitData(data.unitData);
        };

        fetchData();
    }, [period]);

    // Paleta de cores alinhada ao Craft Design
    const COLORS = ['#212529', '#495057', '#adb5bd', '#dee2e6']; 

    return (
        <div className="relatorios-container p-3">
            <h2 className="mb-4">Relatórios Analíticos</h2>
            
            <Form.Group as={Row} className="mb-4 align-items-center">
                <Form.Label column sm={2} className="fw-bold">Período de Análise</Form.Label>
                <Col sm={4}>
                    <Form.Select 
                        className="rounded-0 shadow-none border-dark" 
                        value={period} 
                        onChange={(e) => setPeriod(e.target.value)}
                    >
                        <option value="last12months">Últimos 12 meses</option>
                        <option value="2026">2026</option>
                        <option value="2025">2025</option>
                        <option value="2024">2024</option>
                    </Form.Select>
                </Col>
            </Form.Group>

            <Row className="g-3">
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Incidentes Abertos</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.openIncidents}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Vulnerabilidades Abertas</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.openVulnerabilities}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Incidentes em Andamento</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.ongoingIncidents}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="g-3 mt-1">
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Vulnerab. em Andamento</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.ongoingVulnerabilities}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Taxa de Resolução (Incidentes)</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.completedIncidentsPercentage.toFixed(1)}%</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6">Taxa de Resolução (Vulnerab.)</Card.Title>
                            <Card.Text className="display-4 fw-bold mb-0">{stats.completedVulnerabilitiesPercentage.toFixed(1)}%</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="g-3 mt-3">
                <Col md={6}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6 mb-4">Distribuição por Categoria</Card.Title>
                            <div style={{ height: 350 }}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <PieChart>
                                        <Pie
                                            data={categoryData}
                                            cx="50%"
                                            cy="50%"
                                            outerRadius={110}
                                            fill="#212529"
                                            dataKey="Incidentes/Vulnerabilidades"
                                            nameKey="name"
                                            label
                                        >
                                            {categoryData.map((entry, index) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip contentStyle={{ borderRadius: 0, borderColor: '#000', boxShadow: 'none' }} />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={6}>
                    <Card className="rounded-0 shadow-none border-dark h-100">
                        <Card.Body>
                            <Card.Title className="text-muted text-uppercase fs-6 mb-4">Distribuição por Unidade</Card.Title>
                            <div style={{ height: 350 }}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart data={unitData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#dee2e6" />
                                        <XAxis dataKey="name" tick={{ fill: '#212529' }} axisLine={{ stroke: '#212529' }} />
                                        <YAxis tick={{ fill: '#212529' }} axisLine={{ stroke: '#212529' }} />
                                        <Tooltip contentStyle={{ borderRadius: 0, borderColor: '#000', boxShadow: 'none' }} cursor={{ fill: '#f8f9fa' }} />
                                        <Bar dataKey="Incidentes/Vulnerabilidades" fill="#212529" radius={[0, 0, 0, 0]} />
                                    </BarChart>
                                </ResponsiveContainer>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default Relatorios;