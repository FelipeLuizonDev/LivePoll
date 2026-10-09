package com.livepoll.mobile

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.livepoll.mobile.api.RetrofitClient
import com.livepoll.mobile.api.VotoRequest
import kotlinx.coroutines.launch

class VotacaoActivity: AppCompatActivity() {

    private lateinit var tvTituloVotacao: TextView
    private lateinit var tvInstrucao: TextView
    private lateinit var rgOpcoes: RadioGroup
    private lateinit var pbVotacao: ProgressBar
    private lateinit var tvMensagemVotacao: TextView
    private lateinit var btnVotar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votacao)

        tvTituloVotacao = findViewById(R.id.tvTituloVotacao)
        tvInstrucao = findViewById(R.id.tvInstrucao)
        rgOpcoes = findViewById(R.id.rgOpcoes)
        pbVotacao = findViewById(R.id.pbVotacao)
        tvMensagemVotacao = findViewById(R.id.tvMensagemVotacao)
        btnVotar = findViewById(R.id.btnVotar)

        val enqueteId = intent.getLongExtra(EXTRA_ID, -1L)
        val titulo = intent.getStringExtra(EXTRA_TITULO )
        val opcoes = intent.getStringArrayListExtra(EXTRA_OPCOES)
        val opcaoIds = intent.getLongArrayExtra(EXTRA_OPCAO_IDS)

        if (
            enqueteId == -1L ||
            titulo.isNullOrBlank() ||
            opcoes.isNullOrEmpty() ||
            opcaoIds == null ||
            opcaoIds.size != opcoes.size
            ) {
            tvTituloVotacao.text = "Enquete indisponível"
            tvInstrucao.text = ""
            tvMensagemVotacao.text = "Não foi possível carregar os dados da enquete."
            btnVotar.isEnabled = false
            return
        }

        tvTituloVotacao.text = titulo

        opcoes.forEachIndexed { index, textoOpcao ->
            val radioButton = RadioButton(this).apply {
                id = View.generateViewId()
                tag = opcaoIds[index]
                text = textoOpcao
                textSize = 16f
                setPadding(0, 12, 0, 12)
            }

            rgOpcoes.addView(radioButton)
        }

        btnVotar.setOnClickListener {
            val opcaoSelecionada = rgOpcoes.checkedRadioButtonId

            if (opcaoSelecionada == -1) {
                tvMensagemVotacao.text = "Selecione uma opção para continuar."
                return@setOnClickListener
            }

            val radioButton = findViewById<RadioButton>(opcaoSelecionada)

            val opcaoId = radioButton.tag as? Long

            if (opcaoId == null) {
                tvMensagemVotacao.text = "Não foi possível identificar a opção selecionada."
                return@setOnClickListener
            }

            btnVotar.isEnabled = false
            pbVotacao.visibility = View.VISIBLE
            tvMensagemVotacao.text = "Enviando seu voto..."

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.criarApi(this@VotacaoActivity)

                    val resposta = api.votar(
                        enqueteId,
                        VotoRequest(opcaoId)
                    )

                    if (resposta.isSuccessful) {
                        val intent = android.content.Intent(
                            this@VotacaoActivity,
                            ResultadosActivity::class.java
                        ).apply {
                            putExtra(
                                ResultadosActivity.EXTRA_ENQUETE_ID,
                                enqueteId
                            )
                        }

                        startActivity(intent)
                        finish()
                    } else {
                        tvMensagemVotacao.text =
                            "Não foi possível registrar o voto. Verifique se a enquete continua aberta."
                        btnVotar.isEnabled = true
                    }
                } catch (e: Exception) {
                    tvMensagemVotacao.text =
                        "Erro de conexão ao enviar o voto. Verifique se a API está funcionando."
                    btnVotar.isEnabled = true
                } finally {
                    pbVotacao.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "com.livepoll.mobile.ID_ENQUETE"
        const val EXTRA_TITULO = "com.livepoll.mobile.TITULO_ENQUETE"
        const val EXTRA_OPCOES = "com.livepoll.mobile.OPCOES_ENQUETE"
        const val EXTRA_OPCAO_IDS = "com.livepoll.mobile.IDS_OPCOES_ENQUETE"
    }
}