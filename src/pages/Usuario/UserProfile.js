import React, { useState, useEffect } from "react";
import { Card, Form, Button, Spinner, Image } from "react-bootstrap";
import "./UserProfile.css";
import { getUserProfile, getUsuarioById, updateUserProfile, updateUsuario } from "../../services/usuarioService";
import ToastMessage from "../../components/ToastMessage/ToastMessage";
import { useAuth } from "../../context/AuthContext";

const UserProfile = () => {
    const { user } = useAuth();
    const [formData, setFormData] = useState({});
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [toast, setToast] = useState({ show: false, message: "", variant: "success" });

    const showToast = (message, variant = "success") => {
      setToast({ show: true, message, variant });
    };

  const hideToast = () => setToast({ ...toast, show: false });

  useEffect(() => {
    const fetchUser = async () => {
      try {
        // 🔹 Dependendo do Auth, pode ser user.uid em vez de user.id
        console.log(user)
        console.log(user.uid)
        const data = await getUsuarioById(user?.uid || user?.uid);
        setFormData(data);
      } catch (error) {
        console.error("Erro ao buscar perfil:", error);
        showToast("Erro ao buscar os dados do usuário", "danger");
      } finally {
        setLoading(false);
      }
    };

    if (user) fetchUser();
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    try {
      await updateUsuario(user?.id || user?.uid, formData);
      showToast("Perfil atualizado com sucesso!", "success");
    } catch (error) {
      console.error("Erro ao atualizar perfil:", error);
      showToast("Erro ao atualizar perfil.", "danger");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="text-center my-5">
        <Spinner animation="border" />
        <div>Carregando perfil...</div>
      </div>
    );
  }

  return (
    <div className="user-profile-container">
      <Card className="shadow-sm p-4">
        <div className="d-flex flex-column align-items-center">
          <Image
            src={formData.fotoUrl || "https://via.placeholder.com/120"}
            roundedCircle
            className="profile-pic mb-3"
          />
          <h4>{formData.nome || "Usuário"}</h4>
          <p className="text-muted">{formData.email || "email@exemplo.com"}</p>
        </div>

        <hr />

        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Nome</Form.Label>
            <Form.Control
              name="nome"
              value={formData.nome || ""}
              onChange={handleChange}
              required
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email || ""}
              onChange={handleChange}
              disabled
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Telefone</Form.Label>
            <Form.Control
              type="text"
              name="telefone"
              value={formData.telefone || ""}
              onChange={handleChange}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Cargo</Form.Label>
            <Form.Control
              name="cargo"
              value={formData.funcao || ""}
              onChange={handleChange}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Foto (URL)</Form.Label>
            <Form.Control
              name="fotoUrl"
              value={formData.fotoUrl || ""}
              onChange={handleChange}
              placeholder="Cole o link da imagem"
            />
          </Form.Group>

          <div className="form-actions d-flex justify-content-end">
            <Button variant="primary" type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Salvar Alterações"}
            </Button>
          </div>
        </Form>
      </Card>

      <ToastMessage
        show={toast.show}
        onClose={hideToast}
        message={toast.message}
        variant={toast.variant}
      />
    </div>
  );
};

export default UserProfile;
