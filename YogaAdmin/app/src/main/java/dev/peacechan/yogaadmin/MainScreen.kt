package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
    val searchResults by viewModel.searchResults.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("yogaClassForm/-1") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        },
        topBar = { TopAppBar(title = { Text("Yoga Classes") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Handle padding to avoid overlapping with the TopAppBar
        ) {
            // Search Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Add padding for the search section
            ) {
                // Search Input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search") },
                        modifier = Modifier.weight(1f)
                    )
                    // Search Button
                    Button(onClick = {
                        when {
                            searchQuery.isNotBlank() -> {
                                viewModel.searchByTeacher(searchQuery)
                                viewModel.searchByDate(searchQuery)
                                viewModel.searchByDayOfWeek(searchQuery)
                            }
                            else -> {
                                viewModel.clearSearch() // Clear results if the search query is empty
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }

                }
            }

            // Display Classes
            val classesToShow = if (searchResults.isNotEmpty()) searchResults else yogaClasses

            if (classesToShow.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("No matching courses found.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(classesToShow) { yogaClass ->
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
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = yogaClass.classType,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider()

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                DetailRow(label = "Day", value = yogaClass.dayOfWeek)
                DetailRow(label = "Time", value = yogaClass.time)
                DetailRow(label = "Duration", value = "${yogaClass.duration} minutes")
                DetailRow(label = "Price", value = "£${yogaClass.price}")
                DetailRow(label = "Capacity", value = "${yogaClass.capacity} attendees")
            }

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
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f)
        )
    }
}
