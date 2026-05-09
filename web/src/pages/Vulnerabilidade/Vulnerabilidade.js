import React, { useState } from "react";
import { Card, Accordion } from "react-bootstrap";
import VulnerabilidadeForm from "./VulnerabilidadeForm";
import VulnerabilidadeList from "./VulnerabilidadeList";

const Vulnerabilidade = () => {
    const [selected, setSelected] = useState(null);
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelected(null);
        setRefresh(!refresh);
    };

    const handleCancel = () => {
        setSelected(null);
    };

    return (
        <Card className="border-0 m-0 p-0 shadow-none rounded-0">
            <Card.Body className="p-0">
                <Accordion defaultActiveKey="0" alwaysOpen className="d-flex flex-column gap-3">
                    <Accordion.Item eventKey="0" className="border-0 shadow-none rounded-0">
                        <Accordion.Header>
                            {selected ? `✏️ Editar Vulnerabilidade #${selected.id}` : "➕ Novo Registo"}
                        </Accordion.Header>
                        <Accordion.Body>
                            <VulnerabilidadeForm 
                                selectedVulnerabilidade={selected} 
                                onSaveSuccess={handleSave} 
                                onCancel={handleCancel}
                            />
                        </Accordion.Body>
                    </Accordion.Item>

                    <Accordion.Item eventKey="1" className="border-0 shadow-none rounded-0">
                        <Accordion.Header>📋 Listagem de Vulnerabilidades</Accordion.Header>
                        <Accordion.Body>
                            <VulnerabilidadeList 
                                refreshSignal={refresh} 
                                onEdit={(v) => {
                                    setSelected(v);
                                    window.scrollTo({ top: 0, behavior: "smooth" });
                                }} 
                            />
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Card.Body>
        </Card>
    );
};

export default Vulnerabilidade;