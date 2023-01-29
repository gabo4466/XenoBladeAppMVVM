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
import com.example.xenobladeappmvvm.ui.viewmodel.AddBladeViewModel
import com.example.xenobladeappmvvm.ui.viewmodel.ListBladesViewModel
import com.example.xenobladeappmvvm.ui.viewmodel.ListUsersViewModel

@Composable
fun AppNavigation() {
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = AppScreens.Login.route) {
        composable(AppScreens.MainMenu.route) { MainMenu(navigationController) }
        composable(AppScreens.AddBlade.route) { AddBlade(navigationController, AddBladeViewModel()) }
        composable(AppScreens.Login.route) { Login(navigationController) }
        composable(AppScreens.ListUsers.route) { ListUsers(navigationController, ListUsersViewModel(navigationController)) }
        composable(AppScreens.ListBlades.route + "/{email}", arguments = listOf(navArgument(name = "email"){type=
            NavType.StringType})) { ListBlades(navigationController, ListBladesViewModel(navigationController, it.arguments?.getString("email"))) }
    }
}