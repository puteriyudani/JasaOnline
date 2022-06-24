package com.puteriyudani.jasaonline.services

import com.puteriyudani.jasaonline.models.DefaultResponse
import com.puteriyudani.jasaonline.models.JasaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JasaService {
    @GET("services")
    fun getJasa() : Call<JasaResponse>

    @GET("services/{id}")
    fun getJasaUser(
        @Path("id") id: Int
    ) : Call<JasaResponse>

    @Multipart
    @POST("services")
    fun addJasa(
        @Part image: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody,
        @Part("nama_jasa") namaJasa: RequestBody,
        @Part("deskripsi_singkat") deskripsiSingkat: RequestBody,
        @Part("uraian_deskripsi") uraianDeskripsi: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("gambar") gambar: RequestBody
    ) : Call<DefaultResponse>
}