package com.example.xenobladeappmvvm.navigation

sealed class AppScreens(val route: String) {
    object Login: AppScreens("login_screen")
    object MainMenu: AppScreens("main_menu")
    object AddBlade: AppScreens("add_blade")
    object DeleteBlade: AppScreens("delete_blade")
    object ModifyBlade: AppScreens("modify_blade")
    object ListBlades: AppScreens("list_blades")
    object SearchBlade: AppScreens("search_blade")
    object ListUsers: AppScreens("list_users")
}