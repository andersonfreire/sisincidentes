import React, { useEffect, useState } from "react";
import { Table, Button, Spinner } from "react-bootstrap";
import { getLicoes, deleteLicao } from "../../services/licaoService";
import ConfirmModal from "../../components/ConfirmModal/ConfirmModal";
import { getUsuarioById } from "../../services/usuarioService";
import { getIncidenteById } from "../../services/incidentService";

const LicaoList = ({ onEdit, refresh }) => {
  const [licoes, setLicoes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [licaoSelecionada, setLicaoSelecionada] = useState(null);

  // Função para buscar e enriquecer as lições com nome do autor e número do incidente
  const fetchLicoes = async () => {
    setLoading(true);
    try {
      const data = await getLicoes();

      // Enriquecer cada lição com dados relacionados
      const licoesComDetalhes = await Promise.all(
        data.map(async (licao) => {
          const [autor, incidente] = await Promise.all([
            licao.autor ? getUsuarioById(licao.autor) : null,
            licao.id_incidente ? getIncidenteById(licao.id_incidente) : null,
          ]);

          return {
            ...licao,
            autorNome: autor?.nome || "—",
            numeroChamado: incidente?.numeroChamado || "—",
          };
        })
      );

      setLicoes(licoesComDetalhes);
    } catch (error) {
      console.error("Erro ao buscar lições aprendidas:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLicoes();
  }, [refresh]);

  const handleDeleteClick = (licao) => {
    setLicaoSelecionada(licao);
    setShowModal(true);
  };

  const handleConfirmDelete = async () => {
    try {
      await deleteLicao(licaoSelecionada.id);
      setShowModal(false);
      setLicaoSelecionada(null);
      fetchLicoes();
    } catch (error) {
      console.error("Erro ao excluir lição:", error);
    }
  };

  const handleCancelDelete = () => {
    setShowModal(false);
    setLicaoSelecionada(null);
  };

  if (loading) {
    return (
      <div className="text-center my-5">
        <Spinner animation="border" role="status" />
        <div>Carregando lições aprendidas...</div>
      </div>
    );
  }

  return (
    <div className="py-4">
      <h4 className="mb-3">Lições Aprendidas Registradas</h4>

      {licoes.length === 0 ? (
        <p className="text-center text-muted">Nenhuma lição registrada.</p>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Título</th>
              <th>Autor</th>
              <th>Incidente</th>
              <th>Data de Registro</th>
              <th>Anexo</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {licoes.map((licao) => (
              <tr key={licao.id}>
                <td>{licao.titulo || "—"}</td>
                <td>{licao.autorNome}</td>
                <td>{licao.numeroChamado}</td>
                <td>{formatarData(licao.data_registro)}</td>
                <td>
                  {licao.anexos ? (
                    <a
                      href={licao.anexos}
                      target="_blank"
                      rel="noreferrer"
                    >
                      Ver documento
                    </a>
                  ) : (
                    "—"
                  )}
                </td>
                <td>
                  <Button
                    variant="warning"
                    size="sm"
                    className="me-2"
                    onClick={() => onEdit(licao)}
                  >
                    Editar
                  </Button>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => handleDeleteClick(licao)}
                  >
                    Excluir
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      {/* Modal de confirmação */}
      <ConfirmModal
        show={showModal}
        onHide={handleCancelDelete}
        onConfirm={handleConfirmDelete}
        title="Confirmar exclusão"
        message={
          licaoSelecionada
            ? `Deseja realmente excluir a lição "${licaoSelecionada.titulo}"?`
            : "Deseja realmente excluir esta lição?"
        }
      />
    </div>
  );
};

// Função utilitária para formatação de data
const formatarData = (timestamp) => {
  if (!timestamp) return "—";
  const date =
    timestamp.toDate?.() instanceof Date
      ? timestamp.toDate()
      : new Date(timestamp);
  return date.toLocaleString("pt-BR");
};

export default LicaoList;
