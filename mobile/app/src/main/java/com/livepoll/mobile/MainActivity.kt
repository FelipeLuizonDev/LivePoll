package com.livepoll.mobile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.livepoll.mobile.adapter.EnqueteAdapter
import com.livepoll.mobile.api.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var pbProgresso: ProgressBar
    private lateinit var tvMensagem: TextView
    private lateinit var rvListarEnquetes: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val handler = Handler(Looper.getMainLooper())

    private val atualizarEnquetesRunnable = object : Runnable {
        override fun run() {
            carregarEnquetes()
            handler.postDelayed(this, 5_000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pbProgresso = findViewById(R.id.pbProgresso)
        tvMensagem = findViewById(R.id.tvMensagem)
        rvListarEnquetes = findViewById(R.id.rvListarEnquetes)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        rvListarEnquetes.layoutManager = LinearLayoutManager(this)

        swipeRefresh.setOnRefreshListener {
            carregarEnquetes()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacks(atualizarEnquetesRunnable)
        carregarEnquetes()
        handler.postDelayed(atualizarEnquetesRunnable, 5_000)
    }

    override fun onPause() {
        handler.removeCallbacks(atualizarEnquetesRunnable)
        super.onPause()
    }
    private fun carregarEnquetes() {
        pbProgresso.visibility = View.VISIBLE
        tvMensagem.text = ""

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.criarApi(this@MainActivity)
                val enquetes = api.listarEnquetes()

                rvListarEnquetes.adapter = EnqueteAdapter(enquetes) { enquete ->
                    val intent = if (enquete.aberta) {
                            android.content.Intent(
                                this@MainActivity, VotacaoActivity::class.java
                            ).apply {
                                putExtra(VotacaoActivity.EXTRA_ID, enquete.id)
                                putExtra(VotacaoActivity.EXTRA_TITULO, enquete.titulo)

                                putExtra(
                                    VotacaoActivity.EXTRA_OPCAO_IDS,
                                    enquete.opcoes.map { it.id }.toLongArray()
                                )

                                putStringArrayListExtra(
                                    VotacaoActivity.EXTRA_OPCOES,
                                    ArrayList(enquete.opcoes.map { it.textoOpcao })
                                )
                        }
                    } else {
                        android.content.Intent(
                            this@MainActivity, ResultadosActivity::class.java
                        ).apply {
                            putExtra(
                                ResultadosActivity.EXTRA_ENQUETE_ID,
                                enquete.id
                            )
                        }
                    }

                    startActivity(intent)
                }

                tvMensagem.text = if (enquetes.isEmpty()) {
                    "Nenhuma enquete encontrada."
                } else {
                    "Conexão com a API realizada! Enquetes recebidas: ${enquetes.size}"
                }
            } catch (e: Exception) {
                tvMensagem.text = "Não foi possível conectar à API. Verifique o backend e a conexão."
            } finally {
                pbProgresso.visibility = View.GONE
                swipeRefresh.isRefreshing = false
            }
        }
    }
}