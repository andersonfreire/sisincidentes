import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Login.css";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../../firebaseConfig";

const Login = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        setErro("");

        try {
            await signInWithEmailAndPassword(auth, email, senha);
            navigate("/");
        } catch (error) {
            switch (error.code) {
                case "auth/invalid-email":
                    setErro("O formato do e-mail é inválido.");
                    break;
                case "auth/user-disabled":
                    setErro("Este usuário foi desabilitado.");
                    break;
                case "auth/user-not-found":
                case "auth/wrong-password":
                case "auth/invalid-credential":
                    setErro("Email ou senha inválidos. Tente novamente.");
                    break;
                default:
                    setErro("Erro ao efetuar login. Verifique suas credenciais.");
                    break;
            }
        }
    };

    return (
        <div className="d-flex flex-column login-page">
            {/* Formulário centralizado */}
            <div className="login-container">
                <div className="card login-card shadow-sm border-0 p-4">
                    <div className="text-center mb-4 login-title">
                        <h3 className="fw-bold text-primary">Login</h3>
                        <p className="text-muted mb-0">Acesse o SisIncidentes</p>
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
                                required
                            />
                        </div>

                        {erro && (
                            <div className="alert alert-danger py-2" role="alert">
                                {erro}
                            </div>
                        )}

                        <button type="submit" className="btn btn-primary w-100">
                            Entrar
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <small className="text-muted">
                        Ainda não tem conta? <a href="#">Cadastre-se</a>
                        </small>
                    </div>
                </div>
            </div>

            {/* Rodapé */}
            <footer className="login-footer">
                <small>© {new Date().getFullYear()} SisIncidentes</small>
            </footer>
        </div>
    );
};

export default Login;
