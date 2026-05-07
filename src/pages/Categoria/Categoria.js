// src/components/Category.js
import React, { useState } from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import CategoriaList from "./CategoriaList";
import CategoriaForm from "./CategoriaForm";
import "./Categoria.css";

const Categoria = () => {
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelectedCategory(null);
        setRefresh(!refresh);
    };

    return (
        <Container className="categoria-page m-0">
            <Card className="categoria-card border-0">
                <Card.Body className="categoria-card-body">                 
                    <CategoriaForm
                        selectedCategory={selectedCategory}
                        setSelectedCategory={setSelectedCategory}
                        onSave={handleSave}
                    />
                
                    <CategoriaList
                        key={refresh}
                        onEdit={(cat) => setSelectedCategory(cat)}
                    />
                </Card.Body>
            </Card>
        </Container>
  );
};

export default Categoria;
