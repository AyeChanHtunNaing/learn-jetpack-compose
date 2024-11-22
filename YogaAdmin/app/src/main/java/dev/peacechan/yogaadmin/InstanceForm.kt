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
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstanceForm(
    navController: NavHostController,
    viewModel: YogaClassViewModel,
    dayOfWeek: String,
    yogaClassId: Int?,
    courseType: String?,
    time: String?
) {
    var selectedDate by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Class Instance") },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // Display Current Course Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Course Type: ${courseType ?: "N/A"}", style = MaterialTheme.typography.titleMedium)
                        Text("Day of Week: $dayOfWeek", style = MaterialTheme.typography.bodyMedium)
                        Text("Time: ${time ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Date Picker Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePickerDialog(
                                context = context,
                                dayOfWeek = dayOfWeek,
                                onDateSelected = { date ->
                                    selectedDate = date
                                    showError = false
                                    errorMessage = ""
                                },
                                onError = { error ->
                                    errorMessage = error
                                    showError = true
                                }
                            )
                        }
                ) {
                    OutlinedTextField(
                        value = if (selectedDate.isEmpty()) "Select Date" else selectedDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Date (Required)") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false // Disable direct input
                    )
                }

                // Error Message (if any)
                if (showError && errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Teacher Input
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher (Required)") },
                    isError = showError && teacher.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Additional Comments
                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Additional Comments (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Submit Button
                Button(
                    onClick = {
                        if (selectedDate.isEmpty() || teacher.isEmpty()) {
                            showError = true
                            errorMessage = "All required fields must be filled."
                        } else {
                            yogaClassId?.let { classId ->
                                val instance = CourseInstance(
                                    id = 0,
                                    yogaClassId = classId,
                                    date = selectedDate,
                                    teacher = teacher,
                                    comments = comments
                                )
                                viewModel.addClassInstance(instance)
                            }
                            navController.navigateUp()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit")
                }
            }
        }
    )
}

fun showDatePickerDialog(
    context: Context,
    dayOfWeek: String,
    onDateSelected: (String) -> Unit,
    onError: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val selectedDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(selectedCalendar.time)
            if (selectedDay.equals(dayOfWeek, ignoreCase = true)) {
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)
                onDateSelected(formattedDate)
            } else {
                onError("Selected date does not match the required day of the week: $dayOfWeek")
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
