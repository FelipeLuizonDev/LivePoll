import api from './api';

export interface Opcao {
    id: number
    textoOpcao: string
}

export interface Enquete {
    id: number
    titulo: string
    aberta: boolean
    opcoes: Opcao[]
}

export async function buscarEnquete(id: number): Promise<Enquete> {
    const response = await api.get<Enquete>(`/enquetes/${id}`)

    return response.data;
}