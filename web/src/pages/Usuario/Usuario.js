// Página principal que orquestra o CRUD de Usuários.

import React, { useState } from "react";
import { Container, Card } from "react-bootstrap";
import UsuarioList from "../../components/UsuarioList";
import UsuarioForm from "../../components/UsuarioForm";
import "./Usuario.css";

const Usuario = () => {
    // Controlar a edição (passado entre Form e List)
    const [selectedUsuario, setSelectedUsuario] = useState(null);
    
    // "Gatilho" para forçar a atualização da lista pós-salvamento
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelectedUsuario(null);
        setRefresh(!refresh); // Inverte o gatilho
    };

    return (
        <Container className="usuario-page m-0">
            <Card className="usuario-card border-0">
                <Card.Body className="usuario-card-body">                 
                    <UsuarioForm
                        selectedUsuario={selectedUsuario}
                        setSelectedUsuario={setSelectedUsuario}
                        onSave={handleSave}
                    />
                
                    <UsuarioList
                        key={refresh} // A lista é recarregada quando a 'key' muda
                        onEdit={(user) => setSelectedUsuario(user)}
                    />
                </Card.Body>
            </Card>
        </Container>
  );
};

export default Usuario;