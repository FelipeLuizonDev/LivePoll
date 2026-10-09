package com.livepoll.mobile.api

import retrofit2.http.GET

data class EnqueteResponse(
    val id: Long,
    val titulo: String,
    val aberta: Boolean,
    val opcoes: List<OpcaoResponse>
)

data class OpcaoResponse(
    val id: Long,
    val textoOpcao: String
)

interface LivePollApi {

    @GET("enquetes")
    suspend fun listarEnquetes(): List<EnqueteResponse>
}