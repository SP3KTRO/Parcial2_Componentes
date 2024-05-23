package com.example.udppc_parcial2.viewModel.appNavegation

sealed class appScreens (val router: String){
    object screenMain: appScreens("screenMain")
    object screenAddPet: appScreens("screenAddPet")
    object screenWelcome: appScreens("screenWelcome")
}