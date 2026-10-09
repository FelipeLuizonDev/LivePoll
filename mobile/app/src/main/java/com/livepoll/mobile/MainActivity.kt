package com.livepoll.mobile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.livepoll.mobile.api.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var progresso: ProgressBar
    private lateinit var mensagem: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progresso = findViewById(R.id.pbProgresso)
        mensagem = findViewById(R.id.tvMensagem)

        carregarEnquetes()
    }

    private fun carregarEnquetes() {
        progresso.visibility = View.VISIBLE
        mensagem.text = ""

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.criarApi(this@MainActivity)
                val enquetes = api.listarEnquetes()

                mensagem.text = if (enquetes.isEmpty()) {
                    "Nenhuma enquete encontrada."
                } else {
                    "Conexão com a API realizada! Enquetes recebidas: ${enquetes.size}"
                }
            } catch (e: Exception) {
                mensagem.text = "Não foi possível conectar à API. Verifique o backend e a conexão."
            } finally {
                progresso.visibility = View.GONE
            }
        }
    }
}