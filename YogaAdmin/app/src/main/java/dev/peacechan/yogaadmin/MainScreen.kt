package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, viewModel: YogaClassViewModel) {
    val yogaClasses by viewModel.yogaClasses.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("yogaClassForm/-1") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        },
        topBar = { TopAppBar(title = { Text("Yoga Classes") }) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // Add 80dp space below the app bar

            if (yogaClasses.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("There are no courses. Add new courses to get started.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(yogaClasses) { yogaClass ->
                        YogaClassItem(
                            yogaClass = yogaClass,
                            onEdit = { navController.navigate("yogaClassForm/${yogaClass.id}") },
                            onDelete = { viewModel.deleteYogaClass(yogaClass) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun YogaClassItem(yogaClass: YogaClass, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Add padding between cards
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Class Type
            Text(
                text = yogaClass.classType,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider() // Add a divider to separate sections

            // Details Section
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                DetailRow(label = "Day", value = yogaClass.dayOfWeek)
                DetailRow(label = "Time", value = yogaClass.time)
                DetailRow(label = "Duration", value = "${yogaClass.duration} minutes")
                DetailRow(label = "Price", value = "£${yogaClass.price}")
                DetailRow(label = "Capacity", value = "${yogaClass.capacity} attendees")
            }

            // Actions (Edit/Delete)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onEdit) {
                    Text("Edit")
                }
                OutlinedButton(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f) // Align labels
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f) // Align values
        )
    }
}