package dev.peacechan.yogaadmin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLocationDisplay(navController: NavHostController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf("Fetching...") }
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Permission request launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isPermissionGranted = isGranted
        if (isGranted) {
            fetchUserLocation(context, fusedLocationClient) { location ->
                userLocation = location ?: "Not available"
            }
        } else {
            userLocation = "Permission denied"
        }
    }

    // Check permission status on composable launch
    LaunchedEffect(Unit) {
        isPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            fetchUserLocation(context, fusedLocationClient) { location ->
                userLocation = location ?: "Not available"
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Display user location or appropriate message
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Location")
                Text(
                    text = userLocation,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isPermissionGranted) {
                    Button(onClick = {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }) {
                        Text("Grant Permission")
                    }
                } else {
                    Button(onClick = {
                        fetchUserLocation(context, fusedLocationClient) { location ->
                            userLocation = location ?: "Not available"
                        }
                    }) {
                        Text("Refresh")
                    }
                }
            }
        }
    }
}

private fun fetchUserLocation(
    context: android.content.Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (String?) -> Unit
) {
    val permissionStatus = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        onLocationFetched("$lat, $lon")
                    } else {
                        onLocationFetched("Not available")
                    }
                }
                .addOnFailureListener { exception ->
                    onLocationFetched("Error fetching location")
                }
        } catch (e: SecurityException) {
            onLocationFetched("Permission error")
        }
    } else {
        onLocationFetched("Permission denied")
    }
}
