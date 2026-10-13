
import { Link, NavLink } from 'react-router-dom'

function Header() {
  return (
    <header className="app-header">
      <div className="header-content">
        <Link to="/" className="brand">
          <span className="brand-icon" aria-hidden="true">
            <svg
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M3 3v18h18" />
              <path d="m19 9-5 5-4-4-4 4" />
            </svg>
          </span>

          <span className="brand-text">
            <strong>LivePoll</strong>
            <small>Votação em tempo real</small>
          </span>
        </Link>

        <nav className="main-nav" aria-label="Navegação principal">
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `nav-link${isActive ? ' active' : ''}`
            }
          >
            Início
          </NavLink>

          <NavLink
            to="/enquetes"
            className={({ isActive }) =>
              `nav-link${isActive ? ' active' : ''}`
            }
          >
            Enquetes
          </NavLink>

          <Link to="/enquetes/nova" className="nav-create">
            + Criar enquete
          </Link>
        </nav>
      </div>
    </header>
  )
}

export default Header
