package com.example.udppc_parcial2.viewModel.appNavegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.udppc_parcial2.view.screenAddPet
import com.example.udppc_parcial2.view.screenMain
import com.example.udppc_parcial2.repository.PetSaveRepository
import com.example.udppc_parcial2.viewModel.ScreenAddPetViewModel

@Composable
fun appNavegation(){
    val context = LocalContext.current
    val service = PetSaveRepository()
    val viewModel = ScreenAddPetViewModel(context,service)
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = appScreens.screenMain.router){

        composable(route= appScreens.screenMain.router){ screenMain(navController) }
        composable(route= appScreens.screenAddPet.router){ screenAddPet(navController,viewModel)}

    }
}