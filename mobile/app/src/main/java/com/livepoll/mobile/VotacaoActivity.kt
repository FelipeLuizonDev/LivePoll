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

        val titulo = intent.getStringExtra(EXTRA_TITULO )
        val opcoes = intent.getStringArrayListExtra(EXTRA_OPCOES)

        if (titulo.isNullOrBlank() || opcoes.isNullOrEmpty()) {
            tvTituloVotacao.text = "Enquete indisponível"
            tvInstrucao.text = ""
            tvMensagemVotacao.text = "Não foi possível carregar os dados da enquete."
            btnVotar.isEnabled = false
            return
        }

        tvTituloVotacao.text = titulo

        opcoes.forEach { textoOpcao ->
            val radioButton = RadioButton(this).apply {
                id = View.generateViewId()
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

            tvMensagemVotacao.text = "Opção selecionada: ${radioButton.text}"
        }
    }

    companion object {
        const val EXTRA_ID = "com.livepoll.mobile.ID_ENQUETE"
        const val EXTRA_TITULO = "com.livepoll.mobile.TITULO_ENQUETE"
        const val EXTRA_OPCOES = "com.livepoll.mobile.OPCOES_ENQUETE"
    }
}