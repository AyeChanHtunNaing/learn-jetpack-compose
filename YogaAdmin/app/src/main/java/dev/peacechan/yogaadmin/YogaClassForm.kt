package dev.peacechan.yogaadmin

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.compose.rememberNavController
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaClassForm(
    navController: NavHostController,
    viewModel: YogaClassViewModel,
    yogaClassId: Int? // Nullable ID for editing or adding
) {
    // Load the yoga class for editing
    val yogaClass = remember(yogaClassId) {
        yogaClassId?.let { id ->
            viewModel.yogaClasses.value.find { it.id == id }
        }
    }

    // Load the instances of the respective yoga class
    val classInstances by viewModel.getInstancesForYogaClass(yogaClassId ?: 0).collectAsState(initial = emptyList())

    // Pre-fill form states with existing data if available
    var selectedDayOfWeek by rememberSaveable { mutableStateOf(yogaClass?.dayOfWeek ?: "") }
    var selectedTime by rememberSaveable { mutableStateOf(yogaClass?.time ?: "") }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var capacity by rememberSaveable { mutableStateOf(yogaClass?.capacity ?: "") }
    var duration by rememberSaveable { mutableStateOf(yogaClass?.duration ?: "") }
    var price by rememberSaveable { mutableStateOf(yogaClass?.price ?: "") }
    var selectedClassType by rememberSaveable { mutableStateOf(yogaClass?.classType ?: "") }
    var description by rememberSaveable { mutableStateOf(yogaClass?.description ?: "") }

    // Validation and confirmation state
    var showError by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val classTypes = listOf("Flow Yoga", "Aerial Yoga", "Family Yoga")

    if (showConfirmation) {
        // Show confirmation screen
        ConfirmationScreen(
            dayOfWeek = selectedDayOfWeek,
            time = selectedTime,
            capacity = capacity,
            duration = duration,
            price = price,
            classType = selectedClassType,
            description = description,
            onEdit = { showConfirmation = false },
            onConfirm = {
                val newClass = YogaClass(
                    id = yogaClass?.id ?: 0,
                    dayOfWeek = selectedDayOfWeek,
                    time = selectedTime,
                    capacity = capacity,
                    duration = duration,
                    price = price,
                    classType = selectedClassType,
                    description = description
                )
                if (yogaClass == null) {
                    viewModel.insertYogaClass(newClass) // Add new class
                } else {
                    viewModel.updateYogaClass(newClass) // Update existing class
                }
                navController.navigateUp() // Navigate back to the main screen
            }
        )
    } else {
        // Show the form
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (yogaClass == null) "Add Yoga Class" else "Edit Yoga Class") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Form Fields
                    item {
                        Spacer(modifier = Modifier.height(100.dp))

                        DropdownField(
                            label = "Day of the Week (Required)",
                            items = daysOfWeek,
                            selectedValue = selectedDayOfWeek,
                            onValueChange = { selectedDayOfWeek = it },
                            showError = showError && selectedDayOfWeek.isEmpty()
                        )

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

                        if (showTimePicker) {
                            TimePickerDialog(
                                onDismiss = { showTimePicker = false },
                                onConfirm = { hour, minute ->
                                    selectedTime = String.format("%02d:%02d", hour, minute)
                                    showTimePicker = false
                                }
                            )
                        }

                        TextFieldWithValidation("Capacity (Required)", capacity, showError && capacity.isEmpty()) { capacity = it }
                        TextFieldWithValidation("Duration (minutes) (Required)", duration, showError && duration.isEmpty()) { duration = it }
                        TextFieldWithValidation("Price (£) (Required)", price, showError && price.isEmpty()) { price = it }

                        Text(
                            text = "Type of Class (Required)",
                            color = if (showError && selectedClassType.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                        )
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

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description (Optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (selectedDayOfWeek.isEmpty() || selectedTime.isEmpty() || capacity.isEmpty() || duration.isEmpty() || price.isEmpty() || selectedClassType.isEmpty()) {
                                    showError = true
                                } else {
                                    showError = false
                                    showConfirmation = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.End)

                        ) {
                            Text("Submit")
                        }

                        Button(
                            onClick = {
                                if (selectedDayOfWeek.isNotEmpty() && yogaClass?.id != null) {
                                    val courseType = selectedClassType
                                    val time = selectedTime
                                    navController.navigate("instanceForm/$selectedDayOfWeek/${yogaClass?.id ?: 0}/$courseType/$time")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Instance")
                        }
                    }

                    // Class Instances
                    if (classInstances.isNotEmpty()) {
                        item {
                            Text("Class Instances", style = MaterialTheme.typography.titleMedium)
                        }
                        items(classInstances) { instance ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Start Date: ${instance.date}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Teacher's Name : ${instance.teacher}", style = MaterialTheme.typography.bodySmall)
                                    if (instance.comments.isNotEmpty()) {
                                        Text("Comments: ${instance.comments}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        TextButton(onClick = {
                                            navController.navigate("updateInstanceForm/${instance.id}")
                                        }) {
                                            Text("Edit")
                                        }
                                        TextButton(onClick = {
                                            viewModel.deleteClassInstance(instance)
                                            if (yogaClass != null) {
                                                navController.navigate("yogaClassForm/${yogaClass.id}")
                                            }

                                        }) {
                                            Text("Delete")
                                        }
                                    }
                                }


                            }
                        }

                    }
                }
            }
        )
    }
}




// Reusable DropdownField
@Composable
fun DropdownField(
    label: String,
    items: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    showError: Boolean = false
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = if (selectedValue.isEmpty()) label else selectedValue,
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                modifier = Modifier.clickable { dropdownExpanded = true }
            )
        },
        label = { Text(label) },
        isError = showError,
        modifier = Modifier.fillMaxWidth()
    )
    DropdownMenu(
        expanded = dropdownExpanded,
        onDismissRequest = { dropdownExpanded = false }
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onValueChange(item)
                    dropdownExpanded = false
                },
                text = { Text(item) }
            )
        }
    }
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
fun TextFieldWithValidation(label: String, value: String, isError: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default,
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConfirmationScreen(
    dayOfWeek: String,
    time: String,
    capacity: String,
    duration: String,
    price: String,
    classType: String,
    description: String,
    onEdit: () -> Unit,
    onConfirm: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Details") }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Day of the Week: $dayOfWeek")
                Text("Time: $time")
                Text("Capacity: $capacity")
                Text("Duration: $duration minutes")
                Text("Price: £$price")
                Text("Class Type: $classType")
                Text("Description: ${if (description.isNotEmpty()) description else "N/A"}")

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onEdit) {
                        Text("Edit")
                    }
                    Button(onClick = onConfirm) {
                        Text("Confirm")
                    }
                }
            }
        }
    )
}
