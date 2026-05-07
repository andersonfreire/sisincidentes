// Serviço de CRUD para Usuários.
// Este serviço lida com Firebase Auth (para criação) e Firestore (para metadados).

import { 
    doc, getDoc, getDocs, orderBy, query, serverTimestamp, updateDoc, deleteDoc, collection, setDoc
} from "firebase/firestore";
// Importa os serviços de Auth e Firestore do firebaseConfig
import { auth, db } from "../firebaseConfig";
import { createUserWithEmailAndPassword } from "firebase/auth";

const COL_NAME = "usuarios";
const colRef = collection(db, COL_NAME);

/**
 * Cadastrar.
 * Este é um processo de duas etapas:
 * 1. Criar o usuário no Firebase Authentication (email/senha).
 * 2. Salvar os metadados (nome, matricula, etc.) no Firestore usando o UID do usuário como ID do documento.
 */
export const createUsuario = async (data) => {
    try {
        // Criar o usuário no Firebase Auth
        const userCredential = await createUserWithEmailAndPassword(auth, data.email, data.senha);
        const uid = userCredential.user.uid;

        // Remove a 'senha' para não salvar no banco de dados
        const { senha, ...metadata } = data; 

        // Passo 2: Salvar os metadados no Firestore
        const payload = {
            ...metadata,
            uid: uid, // Armazena o UID de autenticação
            createdAt: serverTimestamp(),
            updatedAt: serverTimestamp(),
            deleted: false
        };
        
        // Usamos setDoc para definir um documento com um ID específico (o UID)
        await setDoc(doc(db, COL_NAME, uid), payload);
        
        return { id: uid, success: true, message: "Usuário criado com sucesso!" };
    } catch (error) {
        console.error("Erro ao criar usuário: ", error);
        // Trata erros comuns do Firebase Auth
        if (error.code === 'auth/email-already-in-use') {
            throw new Error("Este e-mail já está em uso.");
        }
        if (error.code === 'auth/weak-password') {
            throw new Error("A senha deve ter no mínimo 6 caracteres.");
        }
        throw error;
    }
};

/**
 * Atualizar.
 * Apenas atualiza os metadados no Firestore.
 * A atualização de e-mail ou senha no Auth é um fluxo complexo
 * e não será implementado neste formulário.
 */
export const updateUsuario = async (id, data) => {
    try {
        // Remove 'email' e 'senha' para garantir que não sejam atualizados no Firestore
        const { email, senha, ...updates } = data;
        
        const docRef = doc(db, COL_NAME, id);
        const payload = {
            ...updates,
            updatedAt: serverTimestamp(),
        };
        await updateDoc(docRef, payload);
        return { success: true, message: "Usuário atualizado com sucesso!" };
    } catch (error) {
        console.error("Erro ao atualizar usuário: ", error);
        throw error;
    } 
};

// Requisito "Consultar" (Listar todos)
export const getUsuarios = async () => {
    try {
        const snap = await getDocs(query(colRef, orderBy("nome", "asc")));
        const usuarios = snap.docs.map(d => ({ id: d.id, ...d.data() }));
        return usuarios;
    } catch (error) {
        console.error("Erro ao buscar usuários: ", error);
        throw error;
    }
};

// Requisito "Consultar" (Buscar por ID - usado na edição)
export const getUsuarioById = async (id) => {
    try {
        const docRef = doc(db, COL_NAME, id);
        const snap = await getDoc(docRef);
        if (!snap.exists()) return null;
        return { id: snap.id, ...snap.data() };
    } catch (error) {
        console.error("Erro ao buscar usuário pelo ID: ", error);
        throw error;
    }
};

/**
 * Excluir.
 * Aqui só será excluído o registro no Firestore.
 * O usuário no Firebase Authentication não será excluído,
 * pois isso requer privilégios de admin ou reautenticação.
 */
export const deleteUsuario = async (id) => {
    try {
        await deleteDoc(doc(db, COL_NAME, id));
        return { success: true, message: "Registro do usuário deletado com sucesso!" };
    } catch (error) {
        console.error("Erro ao deletar usuário: ", error);
        throw error;
    }
};