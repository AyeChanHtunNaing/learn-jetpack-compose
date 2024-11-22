package dev.peacechan.todoapp.ui

package com.example.tasklistapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskList(
    tasks: List<Task>,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    if (tasks.isEmpty()) {
        Text(
            text = "No tasks available",
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Column {
            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onEdit = { onEdit(task) },
                    onDelete = { onDelete(task) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        backgroundColor = Color.White.copy(alpha = 0.15f),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = task.name, color = Color.White)
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
            }
        }
    }
}
