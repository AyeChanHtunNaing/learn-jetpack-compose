package dev.peacechan.yogaadmin

class YogaClassRepository(private val dao: YogaClassDao) {
    suspend fun insertYogaClass(yogaClass: YogaClass) = dao.insertYogaClass(yogaClass)
    suspend fun updateYogaClass(yogaClass: YogaClass) = dao.updateYogaClass(yogaClass)
    suspend fun deleteYogaClass(yogaClass: YogaClass) = dao.deleteYogaClass(yogaClass)
    suspend fun resetDatabase() = dao.resetDatabase()
    suspend fun getAllYogaClasses() = dao.getAllYogaClasses()
}
