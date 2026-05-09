import { request } from "./api";
const ENDPOINT = "/vulnerabilidades";

export const getVulnerabilidades = () => request(ENDPOINT);

export const createVulnerabilidade = (data) => request(ENDPOINT, {
    method: "POST",
    body: JSON.stringify(data)
});

export const updateVulnerabilidade = (id, data) => request(`${ENDPOINT}/${id}`, {
    method: "PUT",
    body: JSON.stringify(data)
});

export const deleteVulnerabilidade = (id) => request(`${ENDPOINT}/${id}`, {
    method: "DELETE"
});