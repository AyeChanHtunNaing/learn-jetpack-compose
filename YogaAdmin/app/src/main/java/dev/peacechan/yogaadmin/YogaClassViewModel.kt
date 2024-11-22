package dev.peacechan.yogaadmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class YogaClassViewModel(private val repository: YogaClassRepository) : ViewModel() {
    private val _yogaClasses = MutableStateFlow<List<YogaClass>>(emptyList())
    val yogaClasses: StateFlow<List<YogaClass>> get() = _yogaClasses

    init {
        loadYogaClasses()
    }

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
}
