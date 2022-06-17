package com.puteriyudani.jasaonline.services

import com.puteriyudani.jasaonline.helpers.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    //create okhttp client
    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
    //create retrofit builder
    private val builder: Retrofit.Builder =
        Retrofit.Builder().baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
    //create retrofit instance
    private val retrofit: Retrofit = builder.build()
    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}