import { useEffect, useState } from 'react'
import { buscarEnquete, type Enquete } from '../services/enqueteService'

function Home() {
  const [enquete, setEnquete] = useState<Enquete | null>(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    async function carregarEnquete() {
      try {
        const params = new URLSearchParams(window.location.search)
        const enqueteId = Number(params.get('id'))

        if (!enqueteId) {
          throw new Error("ID da enquete não informado.")
        }
        
        const dados = await buscarEnquete(enqueteId)
        setEnquete(dados)
      } catch {
        setErro('Não foi possível carregar a enquete.')
      } finally {
        setCarregando(false)
      }
    }  

    carregarEnquete()
  }, [])

  if (carregando) {
    return <p>Carregando...</p>
  }

  if (erro) {
    return <p>{erro}</p>
  }

  if (!enquete) {
    return <p>Enquete não encontrada.</p>
  }

  return (
    <main>
      <h1>{enquete.titulo}</h1>

      <p>
        Status: {enquete.aberta ? 'Aberta' : 'Fechada'}
      </p>

      <h2>Opções</h2>

      <ul>
        {enquete.opcoes.map((opcao) => (
          <li key={opcao.id}>{opcao.textoOpcao}</li>
        ))}
      </ul>
    </main>
  )
}

export default Home