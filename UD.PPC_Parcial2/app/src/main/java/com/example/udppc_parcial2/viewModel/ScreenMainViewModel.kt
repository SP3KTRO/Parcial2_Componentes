package com.example.udppc_parcial2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udppc_parcial2.network.RetrofitPet
import com.example.udppc_parcial2.repository.GetPetRepository
import kotlinx.coroutines.launch

class ScreenMainViewModel : ViewModel() {
    private val petsApi = RetrofitPet.petsApi
    private val petRepository = GetPetRepository()

    fun searchPets(name: String, onResult: (List<PetDTO>) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val pets = petRepository.searchPets(name)
                onResult(pets)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun fetchPets(sortBy: String, onResult: (List<PetDTO>) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val pets = petsApi.listPets(sortBy)
                onResult(pets)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}