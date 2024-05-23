package com.example.udppc_parcial2.repository

import com.example.udppc_parcial2.viewModel.PetDTO
import com.example.udppc_parcial2.network.PetsApi
import com.example.udppc_parcial2.network.RetrofitPet

class GetPetRepository {
    private val petsApi: PetsApi = RetrofitPet.petsApi

    private suspend fun listPets(sortBy: String): List<PetDTO> {
        return petsApi.listPets(sortBy)
    }
    suspend fun searchPets(name: String): List<PetDTO> {
        return listPets(name).filter { it.name.contains(name, ignoreCase = true) }
    }
}