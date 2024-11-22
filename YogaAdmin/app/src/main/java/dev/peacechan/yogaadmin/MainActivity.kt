package dev.peacechan.yogaadmin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.peacechan.yogaadmin.AppNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalYogaApp()
        }
    }
}

@Composable
fun UniversalYogaApp() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        AppNavigator(navController)
    }
}
