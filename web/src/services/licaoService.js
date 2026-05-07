import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDocs,
  getDoc,
  updateDoc,
  serverTimestamp,
  query,
  orderBy,
} from "firebase/firestore";
import { db } from "../firebaseConfig";

export const createLicao = async (licaoData) => {
    const lessonsRef = collection(db, "lessons");
    await addDoc(lessonsRef, {
        ...licaoData,
        data_registro: serverTimestamp(),
    });
};

export const getLicoes = async () => {
    const lessonsRef = collection(db, "lessons");
    const q = query(lessonsRef, orderBy("data_registro", "desc"));
    const snapshot = await getDocs(q);
    return snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
};

export const getLicaoById = async (id) => {
    const lessonRef = doc(db, "lessons", id);
    const snapshot = await getDoc(lessonRef);
    if (snapshot.exists()) {
        return { id: snapshot.id, ...snapshot.data() };
    } else {
        throw new Error("Lição não encontrada");
    }
};

export const updateLicao = async (id, updatedData) => {
    const lessonRef = doc(db, "lessons", id);
    await updateDoc(lessonRef, updatedData);
};

export const deleteLicao = async (id) => {
    const lessonRef = doc(db, "lessons", id);
    await deleteDoc(lessonRef);
};
