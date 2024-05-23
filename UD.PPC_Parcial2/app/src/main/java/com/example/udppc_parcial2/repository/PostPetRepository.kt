package com.example.udppc_parcial2.repository

import com.example.udppc_parcial2.viewModel.Pet

interface PostPetRepository {
    fun save (pet: Pet)

}