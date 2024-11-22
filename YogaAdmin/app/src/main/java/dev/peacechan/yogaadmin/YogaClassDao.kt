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

    // Search by Teacher Name
    @Query("""
        SELECT yoga_classes.* 
        FROM yoga_classes 
        INNER JOIN course_instances ON yoga_classes.id = course_instances.yogaClassId
        WHERE course_instances.teacher LIKE :name
        ORDER BY course_instances.date
    """)
    suspend fun searchByTeacherName(name: String): List<YogaClass>

    // Search by Date
    @Query("""
        SELECT yoga_classes.* 
        FROM yoga_classes 
        INNER JOIN course_instances ON yoga_classes.id = course_instances.yogaClassId
        WHERE course_instances.date = :date
    """)
    suspend fun searchByDate(date: String): List<YogaClass>

    // Search by Day of the Week
    @Query("""
        SELECT yoga_classes.* 
        FROM yoga_classes 
        WHERE yoga_classes.dayOfWeek = :dayOfWeek
        ORDER BY yoga_classes.time
    """)
    suspend fun searchByDayOfWeek(dayOfWeek: String): List<YogaClass>
    }


