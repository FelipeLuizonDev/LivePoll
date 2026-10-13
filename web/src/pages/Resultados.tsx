
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
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

    if (!Number.isInteger(enqueteId) || enqueteId <= 0) {
      setErro('Informe um ID de enquete válido.')
      setCarregando(false)
      return
    }

    let ativo = true

    async function carregarResultados() {
      try {
        const dados = await buscarResultados(enqueteId)

        if (ativo) {
          setResultado(dados)
          setErro(null)
        }
      } catch {
        if (ativo) {
          setErro('Não foi possível atualizar os resultados. Verifique a conexão com a API.')
        }
      } finally {
        if (ativo) {
          setCarregando(false)
        }
      }
    }

    carregarResultados()

    const intervalo = window.setInterval(() => {
      carregarResultados()
    }, 3000)

    return () => {
      ativo = false
      window.clearInterval(intervalo)
    }
  }, [])

  if (carregando) {
    return (
      <main className="results-page">
        <p className="page-feedback">Carregando resultados...</p>
      </main>
    )
  }

  if (erro && !resultado) {
    return (
      <main className="results-page">
        <div className="results-error">
          <h1>Não foi possível carregar os resultados</h1>
          <p>{erro}</p>
          <Link to="/enquetes" className="button-secondary">
            Voltar para enquetes
          </Link>
        </div>
      </main>
    )
  }

  if (!resultado) {
    return (
      <main className="results-page">
        <p className="page-feedback">Resultados não encontrados.</p>
      </main>
    )
  }

  const totalVotos = resultado.opcoes.reduce(
    (total, opcao) => total + opcao.votos,
    0,
  )

  return (
    <main className="results-page">
      <div className="results-heading">
        <Link to="/enquetes" className="back-link">
          ← Voltar para enquetes
        </Link>

        <span className="home-eyebrow">ACOMPANHAMENTO DA VOTAÇÃO</span>

        <h1>{resultado.titulo}</h1>

        <div className="results-meta">
          <span
            className={`status-badge ${
              resultado.aberta ? 'status-open' : 'status-closed'
            }`}
          >
            <span className="status-dot" aria-hidden="true" />
            {resultado.aberta ? 'Enquete aberta' : 'Enquete fechada'}
          </span>

          <span className="results-live">
            <span className="live-dot" aria-hidden="true" />
            Atualização automática a cada 3 segundos
          </span>
        </div>
      </div>

      {erro && (
        <p className="page-feedback error-feedback" role="status">
          {erro} Exibindo os últimos resultados carregados.
        </p>
      )}

      <section className="total-votes-card">
        <span className="total-votes-label">Total de votos</span>
        <strong>{totalVotos}</strong>
        <span className="total-votes-caption">
          {totalVotos === 1 ? 'voto registrado' : 'votos registrados'}
        </span>
      </section>

      <section className="results-card">
        <div className="results-card-heading">
          <div>
            <h2>Resultados por opção</h2>
            <p>Distribuição dos votos registrados nesta enquete.</p>
          </div>
        </div>

        {totalVotos === 0 ? (
          <div className="results-empty">
            <span className="feature-icon" aria-hidden="true">↗</span>
            <h3>Ainda não há votos</h3>
            <p>
              Os resultados aparecerão aqui assim que alguém votar.
            </p>
          </div>
        ) : (
          <div className="results-options">
            {resultado.opcoes.map((opcao, index) => {
              const percentual = (opcao.votos / totalVotos) * 100

              return (
                <article className="result-option" key={opcao.opcaoId}>
                  <div className="result-option-heading">
                    <div className="result-option-name">
                      <span className="result-option-number">
                        {index + 1}
                      </span>
                      <strong>{opcao.textoOpcao}</strong>
                    </div>

                    <div className="result-option-stats">
                      <strong>{percentual.toLocaleString('pt-BR', {
                        maximumFractionDigits: 1,
                      })}%</strong>
                      <span>
                        {opcao.votos}{' '}
                        {opcao.votos === 1 ? 'voto' : 'votos'}
                      </span>
                    </div>
                  </div>

                  <div
                    className="result-progress-track"
                    role="progressbar"
                    aria-label={`Votos para ${opcao.textoOpcao}`}
                    aria-valuemin={0}
                    aria-valuemax={100}
                    aria-valuenow={percentual}
                  >
                    <div
                      className="result-progress-fill"
                      style={{ width: `${percentual}%` }}
                    />
                  </div>
                </article>
              )
            })}
          </div>
        )}
      </section>
    </main>
  )
}

export default Resultados
