import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDoc,
  getDocs,
  orderBy,
  query,
  serverTimestamp,
  updateDoc,
} from "firebase/firestore";
import { db } from "../firebaseConfig";

const incidentCollection = collection(db, "incidentes");

export const createIncidente = async (data) => {
    try {
        const incidenteData = {
            numeroChamado: data.numeroChamado || "",
            tarefaRelacionada: data.tarefaRelacionada || "",
            tipo: data.tipo || "",
            assunto: data.assunto || "",
            categoriaId: data.categoriaId || "",
            unidadeId: data.unidadeId || "",
            autorId: data.autorId || "",
            atribuidoId: data.atribuidoId || "",
            ipOrigem: data.ipOrigem || "",
            ipDestino: data.ipDestino || "",
            host: data.host || "",
            tempoEstimado: data.tempoEstimado || "",
            cc: data.cc || "",
            descricao: data.descricao || "",
            notas: data.notas || "",
            situacao: data.situacao || "Aberta",
            prioridade: data.prioridade || "Média",
            natureza: data.natureza || "",
            createdAt: serverTimestamp(),
            updatedAt: serverTimestamp(),
            dataConclusao: null,
        };

        const docRef = await addDoc(incidentCollection, incidenteData);
        return { id: docRef.id };
    } catch (error) {
        console.error("Erro ao criar incidente:", error);
        throw error;
    }
};

export const updateIncidente = async (id, data) => {
    try {
        const docRef = doc(db, "incidentes", id);

        const updateData = {
            ...data,
            updatedAt: serverTimestamp(),
        };

        if (data.situacao === "Concluída") {
            updateData.dataConclusao = serverTimestamp();
        }

        await updateDoc(docRef, updateData);
        return true;
    } catch (error) {
        console.error("Erro ao atualizar incidente:", error);
        throw error;
    }
};

export const getIncidentes = async () => {
    try {
        const q = query(incidentCollection, orderBy("createdAt", "desc"));
        const snapshot = await getDocs(q);
        return snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
    } catch (error) {
        console.error("Erro ao buscar incidentes:", error);
        throw error;
    }
};

export const getIncidenteById = async (id) => {
    try {
        const docRef = doc(db, "incidentes", id);
        const docSnap = await getDoc(docRef);
        if (!docSnap.exists()) throw new Error("Incidente não encontrado.");
            return { id: docSnap.id, ...docSnap.data() };
    } catch (error) {
        console.error("Erro ao buscar incidente:", error);
        throw error;
    }
};

export const deleteIncidente = async (id) => {
    try {
        const docRef = doc(db, "incidentes", id);
        await deleteDoc(docRef);
        return true;
    } catch (error) {
        console.error("Erro ao deletar incidente:", error);
        throw error;
    }
};
