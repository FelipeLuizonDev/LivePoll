import { useEffect, useState } from 'react'
import {
  buscarResultados,
  type ResultadoEnquete,
} from '../services/enqueteService'

function Resultados() {
  const [resultado, setResultado] = useState<ResultadoEnquete | null>(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const enqueteId = Number(params.get('id'))

    if (!enqueteId) {
      setErro('ID da enquete não informado.')
      setCarregando(false)
      return
    }

    async function carregarResultados() {
      try {
        const dados = await buscarResultados(enqueteId)

        setResultado(dados)
        setErro(null)
      } catch {
        setErro('Não foi possível carregar os resultados.')
      } finally {
        setCarregando(false)
      }
    }

    carregarResultados()

    const intervalo = setInterval(() => {
      carregarResultados()
    }, 3000)

    return () => {
      clearInterval(intervalo)
    }
  }, [])

  if (carregando) {
    return <p>Carregando resultados...</p>
  }

  if (erro && !resultado) {
    return <p>{erro}</p>
  }

  if (!resultado) {
    return <p>Resultados não encontrados.</p>
  }

  const totalVotos = resultado.opcoes.reduce(
    (total, opcao) => total + opcao.votos,
    0,
  )

  return (
    <main>
      <h1>{resultado.titulo}</h1>

      <p>Total de votos: {totalVotos}</p>

      <h2>Resultados</h2>

      <ul>
        {resultado.opcoes.map((opcao) => (
          <li key={opcao.opcaoId}>
            <strong>{opcao.textoOpcao}</strong>

            <p>Votos: {opcao.votos}</p>
          </li>
        ))}
      </ul>

      {erro && <p>{erro}</p>}
    </main>
  )
}

export default Resultados