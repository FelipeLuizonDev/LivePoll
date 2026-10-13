
import { Link } from 'react-router-dom'

function Home() {
  return (
    <main className="home-page">
      <section className="home-hero">
        <span className="home-eyebrow">
          VOTAÇÃO EM TEMPO REAL
        </span>

        <h1>
          Suas ideias.
          <br />
          Seus votos.
          <br />
          <span>Seus resultados.</span>
        </h1>

        <p className="home-description">
          Crie enquetes, acompanhe as votações e visualize
          os resultados em tempo real com o LivePoll.
        </p>

        <div className="home-actions">
          <Link to="/enquetes/nova" className="button-primary">
            + Criar enquete
          </Link>

          <Link to="/enquetes" className="button-secondary">
            Ver enquetes
          </Link>
        </div>
      </section>

      <section className="home-features">
        <article className="feature-card">
          <span className="feature-icon">＋</span>
          <h2>Crie enquetes</h2>
          <p>
            Cadastre uma pergunta e suas opções de votação
            de forma simples.
          </p>
          <Link to="/enquetes/nova">Criar uma enquete →</Link>
        </article>

        <article className="feature-card">
          <span className="feature-icon">☷</span>
          <h2>Gerencie votações</h2>
          <p>
            Consulte suas enquetes e controle quando
            elas estão abertas ou fechadas.
          </p>
          <Link to="/enquetes">Gerenciar enquetes →</Link>
        </article>

        <article className="feature-card">
          <span className="feature-icon">↗</span>
          <h2>Acompanhe resultados</h2>
          <p>
            Visualize os votos registrados e acompanhe
            a evolução dos resultados.
          </p>
          <Link to="/enquetes">Ver resultados →</Link>
        </article>
      </section>
    </main>
  )
}

export default Home
