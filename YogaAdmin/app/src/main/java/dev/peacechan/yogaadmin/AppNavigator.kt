package dev.peacechan.yogaadmin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Navigation.MainRoute) {
        composable(Navigation.MainRoute) { MainScreen(navController) }
        composable(Navigation.YogaClassFormRoute) { YogaClassForm(navController) }
    }
}