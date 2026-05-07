import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import { createLicao, updateLicao } from "../../services/licaoService";
import { getIncidentes } from "../../services/incidentService";
import ToastMessage from "../../components/ToastMessage/ToastMessage";
import { getUsuarios } from "../../services/usuarioService";

const LicaoForm = ({ selectedLesson, onSave }) => {
    const [formData, setFormData] = useState({
        titulo: "",
        descricao: "",
        autor: "",
        id_incidente: "",
        anexos: "",
    });

    const [incidentes, setIncidentes] = useState([]);
    const [usuarios, setUsuarios] = useState([])
    const [loading, setLoading] = useState(false);
    const [toast, setToast] = useState({ show: false, message: "", variant: "success" });

    const showToast = (message, variant = "success") => {
        setToast({ show: true, message, variant });
    };

    const hideToast = () => setToast({ ...toast, show: false });

    // Buscar incidentes
    useEffect(() => {
        const fetchData = async () => {
            try {
                const [incs, users] = await Promise.all([
                    getIncidentes(),
                    getUsuarios(),
                ]);
                setIncidentes(incs);
                setUsuarios(users);

            } catch (error) {
                console.error("Erro ao carregar incidentes:", error);
                showToast("Erro ao carregar lista de incidentes.", "danger");
            }
        };

        fetchData();
    }, []);

  // Preencher dados ao editar
  useEffect(() => {
    if (selectedLesson) setFormData(selectedLesson);
  }, [selectedLesson]);

  // Atualiza estado conforme o usuário digita
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Salvar / Atualizar lição
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (selectedLesson) {
        await updateLicao(selectedLesson.id, formData);
        showToast("Lição atualizada com sucesso!");
      } else {
        await createLicao(formData);
        showToast("Lição adicionada com sucesso!");
      }

      onSave();
      setFormData({
        titulo: "",
        descricao: "",
        autor: "",
        id_incidente: "",
        anexos: "",
      });
    } catch (error) {
      console.error("Erro ao salvar lição:", error);
      showToast("Erro ao salvar lição.", "danger");
    } finally {
      setLoading(false);
    }
  };

  // Cancelar edição / limpar formulário
  const handleCancel = () => {
    setFormData({
      titulo: "",
      descricao: "",
      autor: "",
      id_incidente: "",
      anexos: "",
    });
    onSave(); // fecha modo de edição se houver
  };

  return (
    <div className="licao-form mb-4">
      <h4>{selectedLesson ? "Editar Lição" : "Nova Lição"}</h4>

      <Form onSubmit={handleSubmit} className="licao-form-inner">
        <Form.Group className="mb-3">
          <Form.Label>Título</Form.Label>
          <Form.Control
            name="titulo"
            value={formData.titulo}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Descrição</Form.Label>
          <Form.Control
            as="textarea"
            name="descricao"
            rows={3}
            value={formData.descricao}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Autor da Lição</Form.Label>
          <Form.Select
            name="autor" // ✅ Corrigido
            value={formData.autor}
            onChange={handleChange}
            required
          >
            <option value="">Selecione...</option>
            {Array.isArray(usuarios) &&
              usuarios.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.nome}
                </option>
              ))}
          </Form.Select>
        </Form.Group>

        

        <Form.Group className="mb-3">
          <Form.Label>Incidente Relacionado</Form.Label>
          <Form.Select
            name="id_incidente" // ✅ Corrigido
            value={formData.id_incidente}
            onChange={handleChange}
            required
          >
            <option value="">Selecione...</option>
            {Array.isArray(incidentes) &&
              incidentes.map((inc) => (
                <option key={inc.id} value={inc.id}>
                  #{inc.numeroChamado || inc.id} — {inc.assunto || "Sem assunto"}
                </option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Anexos (link de documento)</Form.Label>
          <Form.Control
            name="anexos"
            value={formData.anexos}
            onChange={handleChange}
            placeholder="URL do documento (opcional)"
          />
        </Form.Group>

        <div className="form-actions d-flex">
          <Button type="submit" variant="primary" disabled={loading}>
            {loading
              ? "Salvando..."
              : selectedLesson
              ? "Atualizar"
              : "Adicionar"}
          </Button>
          <Button
            variant="secondary"
            className="ms-2"
            onClick={handleCancel}
            disabled={loading}
          >
            Cancelar
          </Button>
        </div>
      </Form>

      <ToastMessage
        show={toast.show}
        onClose={hideToast}
        message={toast.message}
        variant={toast.variant}
      />
    </div>
  );
};

export default LicaoForm;
