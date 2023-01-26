package com.example.xenobladeappmvvm.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.xenobladeappmvvm.ui.screens.*

@Composable
fun AppNavigation() {
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = AppScreens.Login.route) {
        composable(AppScreens.MainMenu.route) { MainMenu(navigationController) }
        composable(AppScreens.AddBlade.route) { AddBlade(navigationController) }
        composable(AppScreens.Login.route) { Login(navigationController) }
        composable(AppScreens.ListUsers.route) { ListUsers(navigationController) }
        composable(AppScreens.ListBlades.route + "/{email}", arguments = listOf(navArgument(name = "email"){type=
            NavType.StringType})) { ListBlades(navigationController, it.arguments?.getString("email")) }
//        composable(AppScreens.ModificarCliente.ruta) {ModificarCliente(navigationController) }
//        composable(AppScreens.BorrarCliente.ruta) { BorrarCliente(navigationController) }
//        composable(AppScreens.ConsultarCliente.ruta) {ConsultarCliente(navigationController) }
//        composable(AppScreens.InformeClientes.ruta) {InformeClientes(navigationController) }
    }
}