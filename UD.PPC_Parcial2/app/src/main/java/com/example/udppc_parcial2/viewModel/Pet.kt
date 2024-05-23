package com.example.udppc_parcial2.viewModel

import android.net.Uri

data class Pet(
    val type: String,
    val name: String,
    val age: Int,
    val breed: String,
    val image: Uri?
) {
}