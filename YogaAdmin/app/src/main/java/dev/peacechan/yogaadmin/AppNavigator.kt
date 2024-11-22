package dev.peacechan.yogaadmin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavigator(navController: NavHostController, viewModel: YogaClassViewModel) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen"
    ) {
        // Main Screen
        composable("mainScreen") {
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
//location
        composable("userLocation") {
            UserLocationDisplay(navController = navController)
        }

        // Yoga Class Form
        composable(
            route = "yogaClassForm/{yogaClassId}",
            arguments = listOf(navArgument("yogaClassId") { type = NavType.StringType })
        ) { backStackEntry ->
            val yogaClassId = backStackEntry.arguments?.getString("yogaClassId")?.toIntOrNull()
            YogaClassForm(
                navController = navController,
                viewModel = viewModel,
                yogaClassId = yogaClassId
            )
        }

        // Instance Form
        composable(
            route = "instanceForm/{dayOfWeek}/{yogaClassId}/{courseType}/{time}",
            arguments = listOf(
                navArgument("dayOfWeek") { type = NavType.StringType },
                navArgument("yogaClassId") { type = NavType.StringType },
                navArgument("courseType") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val dayOfWeek = backStackEntry.arguments?.getString("dayOfWeek") ?: ""
            val yogaClassId = backStackEntry.arguments?.getString("yogaClassId")?.toIntOrNull()
            val courseType = backStackEntry.arguments?.getString("courseType") ?: "N/A"
            val time = backStackEntry.arguments?.getString("time") ?: "N/A"

            InstanceForm(
                navController = navController,
                viewModel = viewModel,
                dayOfWeek = dayOfWeek,
                yogaClassId = yogaClassId,
                courseType = courseType,
                time = time
            )
        }

        // Update Instance Form
        composable(
            route = "updateInstanceForm/{instanceId}",
            arguments = listOf(navArgument("instanceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val instanceId = backStackEntry.arguments?.getString("instanceId")?.toIntOrNull()
            UpdateInstanceForm(
                navController = navController,
                viewModel = viewModel,
                instanceId = instanceId
            )
        }
    }

}
