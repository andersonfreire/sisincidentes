import React from "react";
import { Toast, ToastContainer } from "react-bootstrap";

const ToastMessage = ({ show, onClose, message, variant = "success" }) => {
    return (
        <ToastContainer
            position="top-center"
            className="p-3"
            style={{
                zIndex: 2000,
                position: "fixed",
                top: "100px",
                right: "0",
                left: "0",
            }}
        >
            <Toast
                bg={variant}
                onClose={onClose}
                show={show}
                delay={3000}
                autohide
            >
                <Toast.Body className="text-white text-center fw-semibold">
                    {message}
                </Toast.Body>
            </Toast>
        </ToastContainer>
    );
};

export default ToastMessage;
