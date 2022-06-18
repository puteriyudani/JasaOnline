package com.puteriyudani.jasaonline.models

data class JasaResponse (
    val message: String,
    val error: Boolean,
    val data: List<Jasa>
)