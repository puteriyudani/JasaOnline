package com.puteriyudani.jasaonline.services

import com.puteriyudani.jasaonline.models.JasaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JasaService {
    @GET("services")
    fun getJasa() : Call<JasaResponse>

    @GET("services/{id}")
    fun getJasaUser(
        @Path("id") id: Int
    ) : Call<JasaResponse>
}