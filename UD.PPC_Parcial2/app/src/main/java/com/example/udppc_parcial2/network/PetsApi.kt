package com.example.udppc_parcial2.network

import com.example.udppc_parcial2.viewModel.PetDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PetsApi{
    @GET("pets")
    suspend fun listPets(
        @Query("sortBy") sortBy: String
    ): List<PetDTO>
    @GET("search")
    suspend fun searchPets(
        @Query("name") name: String
    ): List<PetDTO>
    @Multipart
    @POST("pets")
    suspend fun save(
        @Part image: MultipartBody.Part,
        @Part ("name") name: RequestBody,
        @Part ("type") type: RequestBody,
        @Part ("age") age: RequestBody,
        @Part ("breed") breed: RequestBody
    )
}