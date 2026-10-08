import { useEffect, useState } from 'react'
import { listarEnquetes, type Enquete } from '../services/enqueteService'
import { Link } from 'react-router'

function Enquetes() {
  const [enquetes, setEnquetes] = useState<Enquete[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

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

  if (carregando) {
    return <p>Carregando enquetes...</p>
  }

  if (erro) {
    return <p>{erro}</p>
  }

  return (
    <main>
      <h1>Enquetes</h1>

      {enquetes.length === 0 ? (
        <p>Nenhuma enquete encontrada.</p>
      ) : (
        <ul>
          {enquetes.map((enquete) => (
            <li key={enquete.id}>
              <Link to={`/?id=${enquete.id}`}>
                <strong>{enquete.titulo}</strong>
              </Link>

              <p>
                Status: {enquete.aberta ? 'Aberta' : 'Fechada'}
              </p>

              <p>Opções: {enquete.opcoes.length}</p>

              <Link to={`/resultados?id=${enquete.id}`}>
                Ver resultados
              </Link>
            </li>
          ))}
        </ul>
      )}
    </main>
  )
}

export default Enquetes