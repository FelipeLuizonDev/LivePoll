
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { criarEnquete as criarEnqueteApi } from '../services/enqueteService'

function CriarEnquete() {
  const [titulo, setTitulo] = useState('')
  const [opcoes, setOpcoes] = useState(['', ''])
  const [erro, setErro] = useState<string | null>(null)
  const [sucesso, setSucesso] = useState(false)
  const [carregando, setCarregando] = useState(false)

  function adicionarOpcao() {
    if (opcoes.length >= 10) return

    setOpcoes([...opcoes, ''])
  }

  function removerOpcao(index: number) {
    if (opcoes.length <= 2) return

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
      setErro('Não foi possível criar a enquete. Tente novamente.')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <main className="create-page">
      <div className="create-heading">
        <span className="home-eyebrow">NOVA VOTAÇÃO</span>
        <h1>Criar enquete</h1>
        <p>
          Cadastre uma pergunta e as opções para começar
          uma nova votação.
        </p>
      </div>

      <section className="create-card">
        <div className="form-section">
          <label className="form-label" htmlFor="titulo">
            Pergunta da enquete
          </label>

          <input
            className="form-input"
            id="titulo"
            type="text"
            value={titulo}
            onChange={(event) => setTitulo(event.target.value)}
            placeholder="Ex.: Qual linguagem você prefere?"
            maxLength={200}
            disabled={carregando}
          />

          <p className="field-hint">
            Use entre 3 e 200 caracteres.
            <span>{titulo.length}/200</span>
          </p>
        </div>

        <div className="form-section">
          <div className="options-heading">
            <div>
              <h2>Opções de resposta</h2>
              <p>Adicione de 2 a 10 opções para votação.</p>
            </div>

            <span className="options-count">
              {opcoes.length}/10
            </span>
          </div>

          <div className="options-list">
            {opcoes.map((opcao, index) => (
              <div className="option-field" key={index}>
                <label
                  className="option-number"
                  htmlFor={`opcao-${index}`}
                >
                  {index + 1}
                </label>

                <input
                  className="form-input"
                  id={`opcao-${index}`}
                  type="text"
                  value={opcao}
                  onChange={(event) =>
                    atualizarOpcao(index, event.target.value)
                  }
                  placeholder={`Digite a opção ${index + 1}`}
                  maxLength={100}
                  disabled={carregando}
                />

                <button
                  type="button"
                  className="remove-option"
                  onClick={() => removerOpcao(index)}
                  disabled={opcoes.length <= 2 || carregando}
                  aria-label={`Remover opção ${index + 1}`}
                  title="Remover opção"
                >
                  ×
                </button>
              </div>
            ))}
          </div>

          <button
            type="button"
            className="add-option"
            onClick={adicionarOpcao}
            disabled={opcoes.length >= 10 || carregando}
          >
            + Adicionar opção
          </button>
        </div>

        {erro && (
          <div className="form-alert form-alert-error" role="alert">
            <strong>Não foi possível continuar</strong>
            <p>{erro}</p>
          </div>
        )}

        {sucesso && (
          <div className="form-alert form-alert-success" role="status">
            <strong>Enquete criada com sucesso!</strong>
            <p>Ela já pode ser consultada na lista de enquetes.</p>
            <Link to="/enquetes">Ver minhas enquetes →</Link>
          </div>
        )}

        <div className="form-footer">
          <Link to="/enquetes" className="button-secondary">
            Cancelar
          </Link>

          <button
            type="button"
            className="button-primary"
            onClick={criarNovaEnquete}
            disabled={carregando}
          >
            {carregando ? 'Criando enquete...' : 'Criar enquete'}
          </button>
        </div>
      </section>
    </main>
  )
}

export default CriarEnquete
