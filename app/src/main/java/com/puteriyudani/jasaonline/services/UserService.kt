package com.puteriyudani.jasaonline.services

import com.puteriyudani.jasaonline.models.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface UserService {
    @GET("login")
    fun loginUser(
        @QueryMap filter: HashMap<String, String>
    ): Call<LoginResponse>
}