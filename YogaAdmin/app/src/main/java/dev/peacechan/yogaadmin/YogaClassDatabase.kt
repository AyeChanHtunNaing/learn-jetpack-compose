package dev.peacechan.yogaadmin

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [YogaClass::class], version = 1, exportSchema = false)
abstract class YogaClassDatabase : RoomDatabase() {
    abstract fun yogaClassDao(): YogaClassDao
}
