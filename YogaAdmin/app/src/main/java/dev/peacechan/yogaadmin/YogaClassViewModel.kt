package dev.peacechan.yogaadmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class YogaClassViewModel(private val repository: YogaClassRepository) : ViewModel() {
    private val _yogaClasses = MutableStateFlow<List<YogaClass>>(emptyList())
    val yogaClasses: StateFlow<List<YogaClass>> get() = _yogaClasses

    private val _classInstances = MutableStateFlow<List<CourseInstance>>(emptyList())
    val classInstances: StateFlow<List<CourseInstance>> get() = _classInstances

    init {
        loadYogaClasses()
    }

    // Methods for Yoga Classes
    private fun loadYogaClasses() {
        viewModelScope.launch {
            _yogaClasses.value = repository.getAllYogaClasses()
        }
    }

    fun insertYogaClass(yogaClass: YogaClass) {
        viewModelScope.launch {
            repository.insertYogaClass(yogaClass)
            loadYogaClasses() // Refresh the list
        }
    }

    fun updateYogaClass(yogaClass: YogaClass) {
        viewModelScope.launch {
            repository.updateYogaClass(yogaClass)
            loadYogaClasses() // Refresh the list
        }
    }

    fun deleteYogaClass(yogaClass: YogaClass) {
        viewModelScope.launch {
            repository.deleteYogaClass(yogaClass)
            loadYogaClasses() // Refresh the list
        }
    }

    // Methods for Class Instances
    private fun loadClassInstances() {
        viewModelScope.launch {
            _classInstances.value = repository.getAllClassInstances()
        }
    }

    fun getInstancesForYogaClass(yogaClassId: Int): StateFlow<List<CourseInstance>> {
        val filteredInstances = MutableStateFlow<List<CourseInstance>>(emptyList())
        viewModelScope.launch {
            filteredInstances.value = repository.getInstancesForClass(yogaClassId)
        }
        return filteredInstances
    }

    fun addClassInstance(courseInstance: CourseInstance) {
        viewModelScope.launch {
            repository.insertClassInstance(courseInstance)
            refreshInstances(courseInstance.yogaClassId)
        }
    }

    fun updateClassInstance(courseInstance: CourseInstance) {
        viewModelScope.launch {
            repository.updateClassInstance(courseInstance)
            refreshInstances(courseInstance.yogaClassId)
        }
    }

    fun deleteClassInstance(courseInstance: CourseInstance) {
        viewModelScope.launch {
            repository.deleteClassInstance(courseInstance)
            refreshInstances(courseInstance.yogaClassId)
            getInstancesForYogaClass(courseInstance.yogaClassId)// Refresh the list
        }
    }

    private fun refreshInstances(yogaClassId: Int) {
        viewModelScope.launch {
            _classInstances.value = repository.getInstancesForClass(yogaClassId)
        }
    }

    //search
    private val _searchResults = MutableStateFlow<List<YogaClass>>(emptyList())
    val searchResults: StateFlow<List<YogaClass>> get() = _searchResults

    fun searchByTeacher(name: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchClassesByTeacherName(name)
        }
    }

    fun searchByDate(date: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchClassesByDate(date)
        }
    }

    fun searchByDayOfWeek(dayOfWeek: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchClassesByDayOfWeek(dayOfWeek)
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
    }
}

