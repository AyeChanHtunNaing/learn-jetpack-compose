package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaClassForm(navController: NavHostController) {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var selectedDayOfWeek by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var capacity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedClassType by remember { mutableStateOf("") } // Selected radio button value
    var description by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val classTypes = listOf("Flow Yoga", "Aerial Yoga", "Family Yoga") // Radio button options

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Yoga Class") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Dropdown for Day of the Week
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (selectedDayOfWeek.isEmpty()) "Select Day of the Week" else selectedDayOfWeek,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown",
                                Modifier.clickable { dropdownExpanded = true }
                            )
                        },
                        label = { Text("Day of the Week (Required)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        daysOfWeek.forEach { day ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedDayOfWeek = day
                                    dropdownExpanded = false
                                },
                                text = { Text(day) }
                            )
                        }
                    }
                }

                // Time Selector for "Time of Course"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                ) {
                    OutlinedTextField(
                        value = if (selectedTime.isEmpty()) "Select Time" else selectedTime,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Time of Course (Required)") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )
                }

                // Time Picker Dialog
                if (showTimePicker) {
                    TimePickerDialog(
                        onDismiss = { showTimePicker = false },
                        onConfirm = { hour, minute ->
                            selectedTime = String.format("%02d:%02d", hour, minute)
                            showTimePicker = false
                        }
                    )
                }

                // Capacity, Duration, Price Inputs
                TextFieldWithValidation("Capacity (Required)", capacity) { capacity = it }
                TextFieldWithValidation("Duration (minutes) (Required)", duration) { duration = it }
                TextFieldWithValidation("Price per Class (£) (Required)", price) { price = it }

                // Radio Button Group for "Type of Class"
                Text("Type of Class (Required)")
                classTypes.forEach { classType ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedClassType = classType }
                    ) {
                        RadioButton(
                            selected = selectedClassType == classType,
                            onClick = { selectedClassType = classType }
                        )
                        Text(text = classType, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Submit Button
                Button(
                    onClick = {
                        // Add validation and form submission logic here
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit")
                }
            }
        }
    )
}

// Custom TimePickerDialog Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = calendar.get(Calendar.MINUTE)
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

@Composable
fun TextFieldWithValidation(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default
    )
}
