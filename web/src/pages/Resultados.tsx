import { useEffect, useState } from "react";
import { buscarResultados, type ResultadoEnquete } from "../services/enqueteService";

function Resultados() {
    const [resultado, setResultado] = useState<ResultadoEnquete | null>(null)
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        async function carregarResultados() {
            try {
                const params = new URLSearchParams(window.location.search)
                const enqueteId = Number(params.get('id'))

                if (!enqueteId) {
                    throw new Error("ID da enquete não informado.")
                }

                const dados = await buscarResultados(enqueteId)
                setResultado(dados)
            } catch {
                setErro('Não foi possível carregar os resultados.')
            } finally {
                setCarregando(false)
            }
        }

        carregarResultados()
    }, [])

    if (carregando) {
        return <p>Carregando resultados...</p>
    }

    if (erro) {
        return <p>{erro}</p>
    }

    if (!resultado) {
        return <p>Resultados não encontrados.</p>
    }

    return (
        <main>
            <h1>{resultado.titulo}</h1>

            <p>
                Total de votos: {' '}
                {resultado.opcoes.reduce((total, opcao) => total + opcao.votos, 0)}
            </p>

            <h2>Resultados</h2>

            <ul>
                {resultado.opcoes.map((opcao) => (
                    <li key={opcao.opcaoId}>
                        <strong>{opcao.textoOpcao}</strong>

                        <p>Votos: {opcao.votos}</p>
                    </li>
                ))}
            </ul>
        </main>
    )
}

export default Resultados