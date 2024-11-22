package dev.peacechan.yogaadmin

import androidx.room.*

@Dao
interface YogaClassDao {
    @Insert
    suspend fun insertYogaClass(yogaClass: YogaClass)

    @Update
    suspend fun updateYogaClass(yogaClass: YogaClass)

    @Delete
    suspend fun deleteYogaClass(yogaClass: YogaClass)

    @Query("DELETE FROM yoga_classes")
    suspend fun resetDatabase()

    @Query("SELECT * FROM yoga_classes ORDER BY id DESC")
    suspend fun getAllYogaClasses(): List<YogaClass>
}
