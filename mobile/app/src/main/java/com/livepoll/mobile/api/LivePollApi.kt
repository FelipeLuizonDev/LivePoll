package com.livepoll.mobile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

data class VotoRequest(
    val opcaoId: Long
)

data class ResultadoEnqueteResponse(
    val enqueteId: Long,
    val titulo: String,
    val aberta: Boolean,
    val opcoes: List<ResultadoOpcaoResponse>
)

data class ResultadoOpcaoResponse(
    val opcaoId: Long,
    val textoOpcao: String,
    val votos: Long
)

interface LivePollApi {

    @GET("enquetes")
    suspend fun listarEnquetes(): List<EnqueteResponse>

    @POST("enquetes/{id}/votos")
    suspend fun votar(
        @Path("id") enqueteId: Long,
        @Body request: VotoRequest
    ) : Response<Unit>

    @GET("enquetes/{id}/resultados")
    suspend fun buscarResultados(
        @Path("id")enqueteId: Long
    ): ResultadoEnqueteResponse
}