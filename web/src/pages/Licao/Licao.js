import React, { useState } from "react";
import { Card, Accordion } from "react-bootstrap";
import LicaoForm from "./LicaoForm";
import LicaoList from "./LicaoList";

const Licao = () => {
    const [selectedLesson, setSelectedLesson] = useState(null);
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelectedLesson(null);
        setRefresh(prev => !prev);
    };

    return (
        <Card className="border-0 m-0 p-0 shadow-none rounded-0">
            <Card.Body className="p-0">
                <Accordion defaultActiveKey="0" alwaysOpen className="d-flex flex-column gap-3">
                    <Accordion.Item eventKey="0" className="border-0 shadow-none rounded-0">
                        <Accordion.Header>
                            {selectedLesson ? `✏️ Edição de Lição #${selectedLesson.id}` : "➕ Registo de Lição"}
                        </Accordion.Header>
                        <Accordion.Body>
                            <LicaoForm selectedLesson={selectedLesson} onSave={handleSave} />
                        </Accordion.Body>
                    </Accordion.Item>

                    <Accordion.Item eventKey="1" className="border-0 shadow-none rounded-0">
                        <Accordion.Header>📋 Base de Conhecimento</Accordion.Header>
                        <Accordion.Body>
                            <LicaoList onEdit={(licao) => {
                                setSelectedLesson(licao);
                                window.scrollTo({ top: 0, behavior: "smooth" });
                            }} refresh={refresh} />
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Card.Body>
        </Card>
    );
};

export default Licao;