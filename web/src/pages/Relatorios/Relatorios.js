import React, { useState, useEffect } from "react";
import { getRelatoriosData } from "../../services/relatoriosService";
import { Form, Row, Col, Card } from "react-bootstrap";
import { PieChart, Pie, Cell, Tooltip, Legend, BarChart, Bar, XAxis, YAxis, CartesianGrid } from 'recharts';
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
                startDate = new Date(.0, 1);
                endDate = new Date(year, 11, 31);
            }

            const { stats, categoryData, unitData } = await getRelatoriosData(startDate, endDate);
            
            setStats(stats);
            setCategoryData(categoryData);
            setUnitData(unitData);
        };

        fetchData();
    }, [period]);

    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

    return (
        <div className="relatorios-container">
            <h2>Relatórios</h2>
            <Form.Group as={Row} className="mb-3">
                <Form.Label column sm={2}>Período</Form.Label>
                <Col sm={10}>
                    <Form.Select value={period} onChange={(e) => setPeriod(e.target.value)}>
                        <option value="last12months">Últimos 12 meses</option>
                        <option value="2024">2024</option>
                        <option value="2023">2023</option>
                    </Form.Select>
                </Col>
            </Form.Group>
            <Row>
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Incidentes Abertos</Card.Title>
                            <Card.Text className="kpi-number">{stats.openIncidents}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Vulnerabilidades Abertas</Card.Title>
                            <Card.Text className="kpi-number">{stats.openVulnerabilities}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Incidentes em Andamento</Card.Title>
                            <Card.Text className="kpi-number">{stats.ongoingIncidents}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <Row className="mt-3">
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Vulnerabilidades em Andamento</Card.Title>
                            <Card.Text className="kpi-number">{stats.ongoingVulnerabilities}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Incidentes Concluídos (Período)</Card.Title>
                            <Card.Text className="kpi-number">{stats.completedIncidentsPercentage.toFixed(2)}%</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Vulnerabilidades Concluídas (Período)</Card.Title>
                            <Card.Text className="kpi-number">{stats.completedVulnerabilitiesPercentage.toFixed(2)}%</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="mt-4">
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Distribuição por Categoria</Card.Title>
                            <PieChart width={400} height={400}>
                                <Pie
                                    data={categoryData}
                                    cx={200}
                                    cy={200}
                                    labelLine={false}
                                    outerRadius={80}
                                    fill="#8884d8"
                                    dataKey="Incidentes/Vulnerabilidades"
                                    nameKey="name"
                                    label
                                >
                                    {categoryData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                                <Legend />
                            </PieChart>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Distribuição por Unidade Administrativa</Card.Title>
                            <BarChart
                                width={400}
                                height={400}
                                data={unitData}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Bar dataKey="Incidentes/Vulnerabilidades" fill="#8884d8" />
                            </BarChart>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default Relatorios;
