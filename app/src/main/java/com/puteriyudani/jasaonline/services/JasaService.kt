package com.puteriyudani.jasaonline.services

import com.puteriyudani.jasaonline.models.JasaResponse
import retrofit2.Call
import retrofit2.http.GET

interface JasaService {
    @GET("services")
    fun getJasa() : Call<JasaResponse>
}