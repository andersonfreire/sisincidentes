import React, { useState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import { createCategory, updateCategory } from "../../services/categoryService";
import { initialCategoriaModel } from "../../models/categoriaModel";
import ToastMessage from "../../components/ToastMessage/ToastMessage";

const CategoriaForm = ({ selectedCategory, setSelectedCategory, onSave }) => {
  const [categoriaData, setCategoriaData] = useState(initialCategoriaModel);
  const [loading, setLoading] = useState(false);

  // Estado do Toast
  const [toast, setToast] = useState({ show: false, message: "", variant: "success" });

  const showToast = (message, variant = "success") => {
    setToast({ show: true, message, variant });
  };

  const hideToast = () => {
    setToast({ ...toast, show: false });
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setCategoriaData((prevData) => ({
      ...prevData,
      [name]:
        type === "number"
          ? parseFloat(value) || 0
          : type === "checkbox"
          ? checked
          : value,
    }));
  };

  useEffect(() => {
    if (selectedCategory) {
      setCategoriaData(selectedCategory);
    } else {
      setCategoriaData(initialCategoriaModel);
    }
  }, [selectedCategory]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (selectedCategory) {
        await updateCategory(selectedCategory.id, categoriaData);
        showToast("Categoria atualizada com sucesso!");
      } else {
        const res = await createCategory(categoriaData);
        showToast(`Categoria criada com sucesso! ID: ${res.id}`);
      }

      setCategoriaData(initialCategoriaModel);
      setSelectedCategory(null);

      if (onSave) onSave();
    } catch (error) {
      showToast(`Erro: ${error.message}`, "danger");
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    setCategoriaData(initialCategoriaModel);
    setSelectedCategory(null);
  };

  return (
    <div className="categoria-form mb-4 py-3">
      <h4 className="mb-4">{selectedCategory ? "Editar Categoria" : "Nova Categoria"}</h4>

      <Form onSubmit={handleSubmit} className="categoria-form-inner">
        <Form.Group className="mb-3">
          <Form.Label>Nome</Form.Label>
          <Form.Control
            type="text"
            name="nome"
            placeholder="Digite o nome da categoria"
            value={categoriaData.nome}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Descrição</Form.Label>
          <Form.Control
            as="textarea"
            rows={4}
            name="descricao"
            placeholder="Digite a descrição da categoria"
            value={categoriaData.descricao}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Tipo</Form.Label>
          <div>
            <Form.Check
              inline
              type="radio"
              label="Incidente"
              name="tipo"
              value="Incidente"
              checked={categoriaData.tipo === "Incidente"}
              onChange={handleChange}
            />
            <Form.Check
              inline
              type="radio"
              label="Vulnerabilidade"
              name="tipo"
              value="Vulnerabilidade"
              checked={categoriaData.tipo === "Vulnerabilidade"}
              onChange={handleChange}
            />
          </div>
        </Form.Group>

        <div className="d-flex flex-wrap gap-2">
          <Button type="submit" variant="primary" disabled={loading}>
            {selectedCategory ? "Atualizar" : "Adicionar"}
          </Button>
          <Button variant="danger" onClick={handleCancel}>
            Cancelar
          </Button>
        </div>
      </Form>

      {/* Toast de feedback */}
      <ToastMessage
        show={toast.show}
        onClose={hideToast}
        message={toast.message}
        variant={toast.variant}
      />
    </div>
  );
};

export default CategoriaForm;
