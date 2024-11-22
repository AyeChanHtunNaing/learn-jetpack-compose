package dev.peacechan.yogaadmin

class FakeYogaClassDao : YogaClassDao {
    private val classes = mutableListOf<YogaClass>()

    override suspend fun insertYogaClass(yogaClass: YogaClass) {
        classes.add(yogaClass)
    }

    override suspend fun updateYogaClass(yogaClass: YogaClass) {
        val index = classes.indexOfFirst { it.id == yogaClass.id }
        if (index != -1) classes[index] = yogaClass
    }

    override suspend fun deleteYogaClass(yogaClass: YogaClass) {
        classes.remove(yogaClass)
    }

    override suspend fun resetDatabase() {
        classes.clear()
    }

    override suspend fun getAllYogaClasses(): List<YogaClass> = classes
}
