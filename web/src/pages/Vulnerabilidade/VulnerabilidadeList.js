import React, { useEffect, useState } from "react";
import { Table, Badge, Spinner, Alert } from "react-bootstrap";
import { getVulnerabilidades } from "../../services/vulnerabilidadeService";

const VulnerabilidadeList = ({ refreshSignal, onEdit }) => {
    const [vulnerabilidades, setVulnerabilidades] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const loadData = async () => {
        setLoading(true);
        setError(null);
        try {
            console.log("Iniciando busca de vulnerabilidades...");
            const data = await getVulnerabilidades();
            console.log("Dados recebidos da API:", data);
            
            // Garante que 'data' seja um array
            setVulnerabilidades(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error("Erro ao carregar vulnerabilidades:", err);
            setError("Não foi possível carregar a lista. Verifique se o backend está online.");
        } finally {
            setLoading(false);
        }
    };

    // Recarrega sempre que o componente monta ou quando o refreshSignal muda
    useEffect(() => {
        loadData();
    }, [refreshSignal]);

    if (loading) return <div className="text-center p-3"><Spinner animation="border" variant="dark" /></div>;
    if (error) return <Alert variant="danger" className="rounded-0">{error}</Alert>;

    return (
        <div className="mt-3">
            {vulnerabilidades.length === 0 ? (
                <Alert variant="warning" className="rounded-0">Nenhuma vulnerabilidade encontrada no sistema.</Alert>
            ) : (
                <Table responsive className="border border-dark mb-0">
                    <thead className="table-dark">
                        <tr>
                            <th className="rounded-0">ID</th>
                            <th>Título</th>
                            <th>Severidade</th>
                            <th className="text-center">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {vulnerabilidades.map(v => (
                            <tr key={v.id} className="align-middle">
                                <td className="fw-bold">#{v.id}</td>
                                <td>{v.titulo}</td>
                                <td>
                                    <Badge bg="dark" className="rounded-0 shadow-none">
                                        {v.severidade}
                                    </Badge>
                                </td>
                                <td className="text-center">
                                    <button 
                                        className="btn btn-sm btn-outline-dark rounded-0 shadow-none"
                                        onClick={() => onEdit(v)}
                                    >
                                        Editar
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
};

export default VulnerabilidadeList;