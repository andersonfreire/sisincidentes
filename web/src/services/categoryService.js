import { 
    addDoc, collection, deleteDoc, doc, getDoc, getDocs, orderBy, query, serverTimestamp, updateDoc, where 
} from "firebase/firestore";
import { db } from "../firebaseConfig";

// Criar nova categoria
export const createCategory = async (data) => {
    try {
        const colRef = collection(db, "categories");
        const payload = {
            nome: data.nome || "",
            descricao: data.descricao || "",
            tipo: data.tipo || "Incidente", // valor padrão
            createdAt: serverTimestamp(),
            updatedAt: serverTimestamp(),
            deleted: false
        };
        const res = await addDoc(colRef, payload);
        return { id: res.id, success: true, message: "Categoria criada com sucesso!" };
    } catch (error) {
        console.error("Erro ao criar a categoria: ", error);
        throw error;
    }
};

// Atualizar categoria existente
export const updateCategory = async (id, updates) => {
    try {
        const docRef = doc(db, "categories", id);
        const payload = {
            ...updates,
            updatedAt: serverTimestamp(),
        };
        await updateDoc(docRef, payload);
        return { success: true, message: "Categoria atualizada com sucesso!" };
    } catch (error) {
        console.error("Erro ao atualizar a categoria: ", error);
        throw error;
    } 
};

// Buscar todas as categorias
export const getCategories = async () => {
    try {
        const colRef = collection(db, "categories");
        const snap = await getDocs(query(colRef, orderBy("createdAt", "desc")));
        const categories = snap.docs.map(d => ({ id: d.id, ...d.data() }));
        return categories;
    } catch (error) {
        console.error("Erro ao buscar categorias: ", error);
        throw error;
    }
};

// Buscar categoria por nome
export const getCategoryByName = async (name) => {
    try {
        const q = query(collection(db, "categories"), where("name", "==", name));
        const snap = await getDocs(q);
        return snap.docs.map(d => ({ id: d.id, ...d.data() }));
    } catch (error) {
        console.error("Erro ao buscar categoria pelo nome: ", error);
        throw error;
    }
};

// Buscar categoria por ID
export const getCategoryById = async (id) => {
    try {
        const docRef = doc(db, "categories", id);
        const snap = await getDoc(docRef);
        if (!snap.exists()) return null;
        return { id: snap.id, ...snap.data() };
    } catch (error) {
        console.error("Erro ao buscar categoria pelo ID: ", error);
        throw error;
    }
};

// Deletar categoria
export const deleteCategory = async (id) => {
    try {
        await deleteDoc(doc(db, "categories", id));
        return { success: true, message: "Categoria deletada com sucesso!" };
    } catch (error) {
        console.error("Erro ao deletar categoria: ", error);
        throw error;
    }
};
