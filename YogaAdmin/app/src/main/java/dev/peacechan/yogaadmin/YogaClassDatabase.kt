package dev.peacechan.yogaadmin

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [YogaClass::class, CourseInstance::class], // Add CourseInstance here
    version = 1, // Ensure you have the correct version
    exportSchema = false
)
abstract class YogaClassDatabase : RoomDatabase() {
    abstract fun yogaClassDao(): YogaClassDao
}
