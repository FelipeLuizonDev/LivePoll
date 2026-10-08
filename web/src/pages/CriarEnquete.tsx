import { useState } from 'react'
import { criarEnquete as criarEnqueteApi } from '../services/enqueteService'

function CriarEnquete() {
  const [titulo, setTitulo] = useState('')
  const [opcoes, setOpcoes] = useState(['', ''])
  const [erro, setErro] = useState<string | null>(null)
  const [sucesso, setSucesso] = useState(false)
  const [carregando, setCarregando] = useState(false)

  function adicionarOpcao() {
    if (opcoes.length >= 10) {
      return
    }

    setOpcoes([...opcoes, ''])
  }

  function removerOpcao(index: number) {
    if (opcoes.length <= 2) {
      return
    }

    setOpcoes(opcoes.filter((_, i) => i !== index))
  }

  function atualizarOpcao(index: number, valor: string) {
    const novasOpcoes = [...opcoes]
    novasOpcoes[index] = valor

    setOpcoes(novasOpcoes)
  }

  async function criarNovaEnquete() {
    setErro(null)
    setSucesso(false)

    const tituloLimpo = titulo.trim()
    const opcoesLimpas = opcoes.map((opcao) => opcao.trim())

    if (tituloLimpo.length < 3 || tituloLimpo.length > 200) {
      setErro('O título deve ter entre 3 e 200 caracteres.')
      return
    }

    if (opcoesLimpas.length < 2 || opcoesLimpas.length > 10) {
      setErro('A enquete deve possuir entre 2 e 10 opções.')
      return
    }

    if (opcoesLimpas.some((opcao) => opcao.length === 0)) {
      setErro('Todas as opções devem ser preenchidas.')
      return
    }

    if (opcoesLimpas.some((opcao) => opcao.length > 100)) {
      setErro('Cada opção pode ter no máximo 100 caracteres.')
      return
    }

    try {
      setCarregando(true)

      await criarEnqueteApi({
        titulo: tituloLimpo,
        opcoes: opcoesLimpas,
      })

      setSucesso(true)
      setTitulo('')
      setOpcoes(['', ''])
    } catch {
      setErro('Não foi possível criar a enquete.')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <main>
      <h1>Nova enquete</h1>

      <div>
        <label htmlFor="titulo">Título</label>

        <input
          id="titulo"
          type="text"
          value={titulo}
          onChange={(event) => setTitulo(event.target.value)}
          placeholder="Digite o título da enquete"
          maxLength={200}
        />
      </div>

      <div>
        <h2>Opções</h2>

        {opcoes.map((opcao, index) => (
          <div key={index}>
            <input
              type="text"
              value={opcao}
              onChange={(event) =>
                atualizarOpcao(index, event.target.value)
              }
              placeholder={`Opção ${index + 1}`}
              maxLength={100}
            />

            <button
              type="button"
              onClick={() => removerOpcao(index)}
              disabled={opcoes.length <= 2}
            >
              Remover
            </button>
          </div>
        ))}

        <button
          type="button"
          onClick={adicionarOpcao}
          disabled={opcoes.length >= 10}
        >
          Adicionar opção
        </button>
      </div>

      {erro && <p>{erro}</p>}

      {sucesso && <p>Enquete criada com sucesso!</p>}

      <button
        type="button"
        onClick={criarNovaEnquete}
        disabled={carregando}
      >
        {carregando ? 'Criando...' : 'Criar enquete'}
      </button>
    </main>
  )
}

export default CriarEnquete