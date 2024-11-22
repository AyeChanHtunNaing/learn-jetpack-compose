package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, viewModel: YogaClassViewModel) {
    val context = LocalContext.current
    val yogaClasses by viewModel.yogaClasses.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val alertMessage by viewModel.alertMessage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("yogaClassForm/-1") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Yoga Classes") },
                actions = {

// Reset Database Button
                    IconButton(onClick = { resetDatabaseWithInternetCheck(context, viewModel) }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reset Database")
                    }

                    // View Location Button
                    IconButton(onClick = { navController.navigate("userLocation") }) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "View Location")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Search Section
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearch = {
                    when {
                        searchQuery.isNotBlank() -> {
                            viewModel.searchByTeacher(searchQuery)
                            viewModel.searchByDate(searchQuery)
                            viewModel.searchByDayOfWeek(searchQuery)
                        }
                        else -> viewModel.clearSearch()
                    }
                }
            )
            SyncButton(viewModel = viewModel)

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

    // AlertDialog for operations
    if (alertMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearAlertMessage() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearAlertMessage() }) {
                    Text("OK")
                }
            },
            title = { Text("Operation Status") },
            text = { Text(alertMessage ?: "") }
        )
    }
}

@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search") },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onSearch) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        }
    }
}
@Composable
fun SyncButton(viewModel: YogaClassViewModel) {
    Button(
        onClick = {
            viewModel.syncToFirebase() // Sync directly
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Sync with Firebase")
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

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

fun syncWithInternetCheck(context: Context, viewModel: YogaClassViewModel) {
    if (isInternetAvailable(context)) {
        viewModel.syncToFirebase()
    } else {
        Toast.makeText(context, "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show()
    }
}

fun resetDatabaseWithInternetCheck(context: Context, viewModel: YogaClassViewModel) {
    if (isInternetAvailable(context)) {
        viewModel.resetDatabase()
    } else {
        Toast.makeText(context, "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show()
    }
}
