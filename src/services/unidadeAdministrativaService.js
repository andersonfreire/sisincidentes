// Serviço de CRUD para Unidades Administrativas.

import { 
    addDoc, collection, deleteDoc, doc, getDoc, getDocs, orderBy, query, serverTimestamp, updateDoc 
} from "firebase/firestore";
import { db } from "../firebaseConfig";

const COL_NAME = "unidades";
const colRef = collection(db, COL_NAME);

// Cadastrar
export const createUnidade = async (data) => {
    try {
        const payload = {
            ...data,
            createdAt: serverTimestamp(),
            updatedAt: serverTimestamp(),
            deleted: false
        };
        const res = await addDoc(colRef, payload);
        return { id: res.id, success: true, message: "Unidade criada com sucesso!" };
    } catch (error) {
        console.error("Erro ao criar a unidade: ", error);
        throw error;
    }
};

// Atualizar
export const updateUnidade = async (id, updates) => {
    try {
        const docRef = doc(db, COL_NAME, id);
        const payload = {
            ...updates,
            updatedAt: serverTimestamp(),
        };
        await updateDoc(docRef, payload);
        return { success: true, message: "Unidade atualizada com sucesso!" };
    } catch (error) {
        console.error("Erro ao atualizar a unidade: ", error);
        throw error;
    } 
};

// Consultar (listar todas)
export const getUnidades = async () => {
    try {
        const snap = await getDocs(query(colRef, orderBy("titulo", "asc")));
        const unidades = snap.docs.map(d => ({ id: d.id, ...d.data() }));
        return unidades;
    } catch (error) {
        console.error("Erro ao buscar unidades: ", error);
        throw error;
    }
};

// Consultar" (buscar por ID)
export const getUnidadeById = async (id) => {
    try {
        const docRef = doc(db, COL_NAME, id);
        const snap = await getDoc(docRef);
        if (!snap.exists()) return null;
        return { id: snap.id, ...snap.data() };
    } catch (error) {
        console.error("Erro ao buscar unidade pelo ID: ", error);
        throw error;
    }
};

// Excluir
export const deleteUnidade = async (id) => {
    try {
        await deleteDoc(doc(db, COL_NAME, id));
        return { success: true, message: "Unidade deletada com sucesso!" };
    } catch (error) {
        console.error("Erro ao deletar unidade: ", error);
        throw error;
    }
};