import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Login.css";
import { useAuth } from "../../context/AuthContext";

const Login = () => {
    const navigate = useNavigate();
    const { login } = useAuth();
    
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");
    const [loading, setLoading] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();
        setErro("");
        setLoading(true);

        const result = await login(email, senha);

        if (result.success) {
            navigate("/");
        } else {
            setErro(result.error || "Falha na autenticação. Verifique suas credenciais.");
            setLoading(false);
        }
    };

    return (
        <div className="d-flex flex-column login-page">
            <div className="login-container">
                <div className="card login-card shadow-sm border-0 p-4">
                    <div className="text-center mb-4 login-title">
                        <h3 className="fw-bold text-primary">SisIncidentes</h3>
                        <p className="text-muted mb-0">Acesse sua conta</p>
                    </div>

                    <form onSubmit={handleLogin}>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">
                                E-mail
                            </label>
                            <input
                                type="email"
                                id="email"
                                className="form-control"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="exemplo@orgao.gov.br"
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label htmlFor="senha" className="form-label">
                                Senha
                            </label>
                            <input
                                type="password"
                                id="senha"
                                className="form-control"
                                value={senha}
                                onChange={(e) => setSenha(e.target.value)}
                                placeholder="Sua senha"
                                required
                            />
                        </div>

                        {erro && (
                            <div className="alert alert-danger py-2" role="alert">
                                {erro}
                            </div>
                        )}

                        <button 
                            type="submit" 
                            className="btn btn-primary w-100"
                            disabled={loading}
                        >
                            {loading ? "Autenticando..." : "Entrar"}
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <small className="text-muted">
                        SisIncidentes — Sistema de Segurança
                        </small>
                    </div>
                </div>
            </div>

            <footer className="login-footer">
                <small>© {new Date().getFullYear()} SisIncidentes</small>
            </footer>
        </div>
    );
};

export default Login;
