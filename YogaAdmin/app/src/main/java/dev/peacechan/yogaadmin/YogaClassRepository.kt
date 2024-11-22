package dev.peacechan.yogaadmin

class YogaClassRepository(private val dao: YogaClassDao) {

    // Yoga Class Methods
    suspend fun insertYogaClass(yogaClass: YogaClass) = dao.insertYogaClass(yogaClass)
    suspend fun updateYogaClass(yogaClass: YogaClass) = dao.updateYogaClass(yogaClass)
    suspend fun deleteYogaClass(yogaClass: YogaClass) = dao.deleteYogaClass(yogaClass)
    suspend fun resetDatabase() = dao.resetDatabase()
    suspend fun getAllYogaClasses() = dao.getAllYogaClasses()

    // Class Instance Methods
    suspend fun getAllClassInstances() = dao.getAllClassInstances()
    suspend fun insertClassInstance(courseInstance: CourseInstance) = dao.insertClassInstance(courseInstance)
    suspend fun updateClassInstance(courseInstance: CourseInstance) = dao.updateClassInstance(courseInstance)
    suspend fun deleteClassInstance(courseInstance: CourseInstance) = dao.deleteClassInstance(courseInstance)
    suspend fun getClassInstancesByYogaClassId(yogaClassId: Int) = dao.getClassInstancesByYogaClassId(yogaClassId)
    suspend fun getInstancesForClass(yogaClassId: Int): List<CourseInstance> {
        return dao.getInstancesForClass(yogaClassId)
    }

    //search
    suspend fun searchClassesByTeacherName(name: String): List<YogaClass> {
        return dao.searchByTeacherName("%$name%")
    }

    suspend fun searchClassesByDate(date: String): List<YogaClass> {
        return dao.searchByDate(date)
    }

    suspend fun searchClassesByDayOfWeek(dayOfWeek: String): List<YogaClass> {
        return dao.searchByDayOfWeek(dayOfWeek)
    }


}
