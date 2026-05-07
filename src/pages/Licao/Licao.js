import React, { useState } from "react";
import { Card, Container } from "react-bootstrap";
import LicaoForm from "./LicaoForm";
import LicaoList from "./LicaoList";
import "./Licao.css";

const Licao = () => {
    const [selectedLesson, setSelectedLesson] = useState(null);
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelectedLesson(null);
        setRefresh((prev) => !prev);
    };

    return (
        <Container className="m-0 licao-page">
            <Card className="licao-card border-0">
                <Card.Body className="licao-card-body">                 
                    <LicaoForm selectedLesson={selectedLesson} onSave={handleSave} />
                    <LicaoList onEdit={setSelectedLesson} refresh={refresh} />
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Licao;
