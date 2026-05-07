// Página principal que orquestra o CRUD de Unidades.

import React, { useState } from "react";
import { Container, Card } from "react-bootstrap";
import UnidadeAdministrativaList from "../../components/UnidadeAdministrativaList";
import UnidadeAdministrativaForm from "../../components/UnidadeAdministrativaForm";

const UnidadeAdministrativa = () => {
    // Estado para controlar a edição 
    const [selectedUnidade, setSelectedUnidade] = useState(null);
    
    // Estado "gatilho" para forçar a atualização da lista pós-salvamento
    const [refresh, setRefresh] = useState(false);

    const handleSave = () => {
        setSelectedUnidade(null);
        setRefresh(!refresh); 
    };

    return (
        <Container className="m-0 p-0">
            <Card className="border-0">
                <Card.Body>                 
                    <UnidadeAdministrativaForm
                        selectedUnidade={selectedUnidade}
                        setSelectedUnidade={setSelectedUnidade}
                        onSave={handleSave}
                    />
                
                    <UnidadeAdministrativaList
                        key={refresh} 
                        onEdit={(ua) => setSelectedUnidade(ua)}
                    />
                </Card.Body>
            </Card>
        </Container>
  );
};

export default UnidadeAdministrativa;