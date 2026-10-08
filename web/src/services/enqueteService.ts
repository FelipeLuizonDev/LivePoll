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

export interface CriarEnqueteRequest {
    titulo: string
    opcoes: string[]
}

export async function buscarEnquete(id: number): Promise<Enquete> {
    const response = await api.get<Enquete>(`/enquetes/${id}`)

    return response.data;
}

export async function listarEnquetes(): Promise<Enquete[]> {
    const response = await api.get<Enquete[]>(`/enquetes`)

    return response.data;
}

export async function criarEnquete(dados: CriarEnqueteRequest): Promise<Enquete> {
    const response = await api.post<Enquete>('/enquetes', dados)

    return response.data;
}