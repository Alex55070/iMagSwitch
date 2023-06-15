package com.app.ms.ui.theme
import com.app.ms.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationService {
    @POST("/login")
    fun login(@Body request: LoginRequest.LoginRequest): Call<LoginResponse>
}