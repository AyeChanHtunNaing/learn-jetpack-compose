package dev.peacechan.yogaadmin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavigator(navController: NavHostController, viewModel: YogaClassViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController, viewModel)
        }
        composable("yogaClassForm/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            YogaClassForm(navController, viewModel, yogaClassId = id)
        }
    }
}