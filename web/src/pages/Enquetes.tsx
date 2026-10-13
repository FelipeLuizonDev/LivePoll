
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  abrirEnquete,
  fecharEnquete,
  listarEnquetes,
  type Enquete,
} from '../services/enqueteService'

function Enquetes() {
  const [enquetes, setEnquetes] = useState<Enquete[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)
  const [alterandoStatus, setAlterandoStatus] = useState<number | null>(null)

  useEffect(() => {
    async function carregarEnquetes() {
      try {
        const dados = await listarEnquetes()
        setEnquetes(dados)
      } catch {
        setErro('Não foi possível carregar as enquetes.')
      } finally {
        setCarregando(false)
      }
    }

    carregarEnquetes()
  }, [])

  async function alterarStatus(enquete: Enquete) {
    try {
      setAlterandoStatus(enquete.id)
      setErro(null)

      const enqueteAtualizada = enquete.aberta
        ? await fecharEnquete(enquete.id)
        : await abrirEnquete(enquete.id)

      setEnquetes((enquetesAtuais) =>
        enquetesAtuais.map((item) =>
          item.id === enquete.id ? enqueteAtualizada : item,
        ),
      )
    } catch {
      setErro('Não foi possível alterar o status da enquete.')
    } finally {
      setAlterandoStatus(null)
    }
  }

  if (carregando) {
    return (
      <main className="polls-page">
        <p className="page-feedback">Carregando enquetes...</p>
      </main>
    )
  }

  return (
    <main className="polls-page">
      <section className="page-heading">
        <div>
          <span className="home-eyebrow">PAINEL DE GERENCIAMENTO</span>
          <h1>Enquetes</h1>
          <p>Gerencie suas votações e acompanhe os resultados.</p>
        </div>

        <Link to="/enquetes/nova" className="button-primary">
          + Criar enquete
        </Link>
      </section>

      {erro && (
        <p className="page-feedback error-feedback" role="alert">
          {erro}
        </p>
      )}

      <div className="polls-summary">
        <strong>{enquetes.length}</strong>
        <span>
          {enquetes.length === 1 ? 'enquete cadastrada' : 'enquetes cadastradas'}
        </span>
      </div>

      {enquetes.length === 0 ? (
        <section className="empty-state">
          <span className="feature-icon" aria-hidden="true">＋</span>
          <h2>Nenhuma enquete por aqui</h2>
          <p>Crie sua primeira enquete para começar a receber votos.</p>
          <Link to="/enquetes/nova" className="button-primary">
            Criar primeira enquete
          </Link>
        </section>
      ) : (
        <section className="polls-list" aria-label="Lista de enquetes">
          {enquetes.map((enquete) => (
            <article className="poll-card" key={enquete.id}>
              <div className="poll-card-main">
                <div className="poll-card-title">
                  <h2>{enquete.titulo}</h2>
                  <span
                    className={`status-badge ${
                      enquete.aberta ? 'status-open' : 'status-closed'
                    }`}
                  >
                    <span className="status-dot" aria-hidden="true" />
                    {enquete.aberta ? 'Aberta' : 'Fechada'}
                  </span>
                </div>

                <p className="poll-card-meta">
                  {enquete.opcoes.length}{' '}
                  {enquete.opcoes.length === 1 ? 'opção' : 'opções'}
                </p>
              </div>

              <div className="poll-card-actions">
                <Link
                  to={`/resultados?id=${enquete.id}`}
                  className="button-secondary"
                >
                  Ver resultados
                </Link>

                <button
                  type="button"
                  className={
                    enquete.aberta ? 'button-danger' : 'button-primary'
                  }
                  onClick={() => alterarStatus(enquete)}
                  disabled={alterandoStatus !== null}
                >
                  {alterandoStatus === enquete.id
                    ? 'Alterando...'
                    : enquete.aberta
                      ? 'Fechar enquete'
                      : 'Abrir enquete'}
                </button>
              </div>
            </article>
          ))}
        </section>
      )}
    </main>
  )
}

export default Enquetes
