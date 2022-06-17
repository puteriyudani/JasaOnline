package com.puteriyudani.jasaonline.models

data class LoginResponse (
    val message: String,
    val error: Boolean,
    val data: User
)