package com.livepoll.mobile

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.livepoll.mobile.api.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ResultadosActivity: AppCompatActivity() {

    private lateinit var tvTituloResultados: TextView
    private lateinit var tvTotalVotos: TextView
    private lateinit var pbResultados: ProgressBar
    private lateinit var tvMensagemResultados: TextView
    private lateinit var llOpcoesResultados: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        tvTituloResultados = findViewById(R.id.tvTituloResultados)
        tvTotalVotos = findViewById(R.id.tvTotalVotos)
        pbResultados = findViewById(R.id.pbResultados)
        tvMensagemResultados = findViewById(R.id.tvMensagemResultados)
        llOpcoesResultados = findViewById(R.id.llOpcoesResultados)

        val enqueteId = intent.getLongExtra(EXTRA_ENQUETE_ID, -1L)

        if (enqueteId == -1L) {
            tvTituloResultados.text = "Resultados indisponíveis"
            tvMensagemResultados.text = "Não foi possível identificar a enquete."
            return
        }

        carregarResultados(enqueteId)
    }

    private fun carregarResultados(enqueteId: Long) {
        pbResultados.visibility = View.VISIBLE
        tvMensagemResultados.text = ""
        llOpcoesResultados.removeAllViews()

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.criarApi(this@ResultadosActivity)
                val resultado = api.buscarResultados(enqueteId)

                tvTituloResultados.text = resultado.titulo

                val totalVotos = resultado.opcoes.sumOf { it.votos }
                tvTotalVotos.text = "Total de votos: $totalVotos"

                if (totalVotos == 0L) {
                    tvMensagemResultados.text = "Esta enquete ainda não recebeu votos."
                }

                resultado.opcoes.forEach { opcao ->
                    val percentual = if (totalVotos > 0) {
                        (opcao.votos.toDouble() / totalVotos * 100).roundToInt()
                    } else {
                        0
                    }

                    val tvOpcao = TextView(this@ResultadosActivity).apply {
                        text = "${opcao.textoOpcao}\n" + "${opcao.votos} votos ($percentual%)"
                        textSize = 18f
                        setPadding(0, 16, 0, 16)
                    }

                    llOpcoesResultados.addView(tvOpcao)
                }
            } catch (e: Exception) {
                tvMensagemResultados.text =
                    "Não foi possível carregar os resultados. Verifique a conexão com a API."
            } finally {
                pbResultados.visibility = View.GONE
            }
        }
    }

    companion object {
        const val EXTRA_ENQUETE_ID = "com.livepoll.mobile.RESULTADOS_ENQUETE_ID"
    }
}