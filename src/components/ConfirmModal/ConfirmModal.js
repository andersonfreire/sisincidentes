import React from "react";
import { Modal, Button } from "react-bootstrap";

const ConfirmModal = ({ show, onHide, onConfirm, title, message }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title || "Confirmar ação"}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{message || "Deseja realmente prosseguir?"}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancelar
                </Button>
                <Button variant="danger" onClick={onConfirm}>
                    Confirmar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ConfirmModal;
