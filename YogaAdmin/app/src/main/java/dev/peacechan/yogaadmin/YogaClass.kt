package dev.peacechan.yogaadmin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yoga_classes")
data class YogaClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: String,
    val time: String,
    val capacity: String,
    val duration: String,
    val price: String,
    val classType: String,
    val description: String? = null
)
