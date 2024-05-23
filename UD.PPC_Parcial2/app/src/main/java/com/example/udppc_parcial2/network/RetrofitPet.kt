package com.example.udppc_parcial2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitPet{
    private const val BASE_URL ="http://localhost:3000/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val petsApi: PetsApi by lazy {
        retrofit.create(PetsApi::class.java)
    }
}