package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
    var courses = remember { mutableStateOf(listOf<String>()) } // Example: replace with actual data source

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Navigation.YogaClassFormRoute) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        }
    ) {
        if (courses.value.isEmpty()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("There are no courses. Add new courses to get started.")
            }
        } else {
            // Display your courses here
            // For example: LazyColumn { items(courses.value) { Text(it) } }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(rememberNavController())
}
