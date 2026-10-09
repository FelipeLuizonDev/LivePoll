package com.livepoll.mobile.api

import android.content.Context
import com.livepoll.mobile.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    fun criarApi(context: Context): LivePollApi {
        val baseUrl = context.getString(R.string.api_base_url)

        return Retrofit.Builder()
            .baseUrl(baseUrl + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LivePollApi::class.java)
    }
}