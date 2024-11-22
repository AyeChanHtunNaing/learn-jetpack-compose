package dev.peacechan.yogaadmin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_instances")
data class CourseInstance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated unique ID
    val yogaClassId: Int, // Foreign key linking to YogaClass
    val date: String, // Date of the instance in "dd/MM/yyyy" format
    val teacher: String, // Name of the teacher
    val comments: String // Optional additional comments
)
