package dev.peacechan.yogaadmin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: YogaClassViewModel = YogaClassViewModel(
            YogaClassRepository(DatabaseProvider.getDatabase(this).yogaClassDao())
        )

        setContent {
            val navController = rememberNavController()
            AppNavigator(navController = navController, viewModel = viewModel)

        }

    }

}