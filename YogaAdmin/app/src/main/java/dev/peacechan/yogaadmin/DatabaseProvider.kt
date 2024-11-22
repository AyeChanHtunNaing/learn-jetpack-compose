package dev.peacechan.yogaadmin

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: YogaClassDatabase? = null

    fun getDatabase(context: Context): YogaClassDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                YogaClassDatabase::class.java,
                "yoga_class_db"
            ).build()
        }
        return INSTANCE!!
    }
}
