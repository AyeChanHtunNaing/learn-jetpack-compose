package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInstanceForm(
    navController: NavHostController,
    viewModel: YogaClassViewModel,
    instanceId: Int?
) {
    val instance = remember(instanceId) {
        instanceId?.let { id ->
            viewModel.classInstances.value.find { it.id == id }
        }
    }

    var selectedDate by rememberSaveable { mutableStateOf(instance?.date ?: "") }
    var teacher by rememberSaveable { mutableStateOf(instance?.teacher ?: "") }
    var comments by rememberSaveable { mutableStateOf(instance?.comments ?: "") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Class Instance") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Date (Required)") },
                    isError = showError && selectedDate.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher (Required)") },
                    isError = showError && teacher.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Additional Comments (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (selectedDate.isEmpty() || teacher.isEmpty()) {
                            showError = true
                        } else {
                            instance?.let {
                                val updatedInstance = it.copy(
                                    date = selectedDate,
                                    teacher = teacher,
                                    comments = comments
                                )
                                viewModel.updateClassInstance(updatedInstance)
                                navController.navigateUp()
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Update")
                }
            }
        }
    )
}