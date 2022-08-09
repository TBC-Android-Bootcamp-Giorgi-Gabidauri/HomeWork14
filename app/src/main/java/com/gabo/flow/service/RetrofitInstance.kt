package com.gabo.flow.service

import com.gabo.flow.model.YesNoModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitInstance {
    private const val BASE_URL = "https://run.mocky.io/"
    private const val BASE_URL_YESNO = "https://yesno.wtf/"
    private val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val yesNoBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_YESNO)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getService() = retrofitInstance.create(ItemService::class.java)
    fun getYesnoService () = yesNoBuilder.create(YesNoApi::class.java)
}

interface YesNoApi {
    @GET("api")
    suspend fun getYesNoModel(): Response<YesNoModel>
}