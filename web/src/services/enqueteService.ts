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

export interface ResultadoOpcao {
    opcaoId: number
    textoOpcao: string
    votos: number
}

export interface ResultadoEnquete {
    enqueteId: number
    titulo: string
    aberta: boolean
    opcoes: ResultadoOpcao[]
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

export async function buscarResultados(enqueteId: number): Promise<ResultadoEnquete> {
    const response = await api.get<ResultadoEnquete>(
        `/enquetes/${enqueteId}/resultados`,
    )

    return response.data;
}

export async function abrirEnquete(id: number): Promise<Enquete> {
    const response = await api.patch<Enquete>(`/enquetes/${id}/abrir`)

    return response.data
}

export async function fecharEnquete(id: number): Promise<Enquete> {
    const response = await api.patch<Enquete>(`/enquetes/${id}/fechar`)

    return response.data
}