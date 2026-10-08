import { Link } from "react-router-dom"

function Header() {
  return (
    <header>
      <h1>LivePoll</h1>

      <nav>
        <Link to="/">Início</Link>
        {' | '}
        <Link to="/enquetes">Enquetes</Link>
      </nav>
    </header>
  )
}

export default Header