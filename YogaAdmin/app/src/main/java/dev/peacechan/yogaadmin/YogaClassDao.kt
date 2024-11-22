package dev.peacechan.yogaadmin

import androidx.room.*

@Dao
interface YogaClassDao {

    // Yoga Class Queries
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

    // Course Instance Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassInstance(courseInstance: CourseInstance)

    @Update
    suspend fun updateClassInstance(courseInstance: CourseInstance)

    @Delete
    suspend fun deleteClassInstance(courseInstance: CourseInstance)

    @Query("SELECT * FROM course_instances ORDER BY id DESC")
    suspend fun getAllClassInstances(): List<CourseInstance>

    @Query("SELECT * FROM course_instances WHERE yogaClassId = :yogaClassId ORDER BY date ASC")
    suspend fun getClassInstancesByYogaClassId(yogaClassId: Int): List<CourseInstance>

    @Query("SELECT * FROM course_instances WHERE yogaClassId = :yogaClassId")
    suspend fun getInstancesForClass(yogaClassId: Int): List<CourseInstance>
}
