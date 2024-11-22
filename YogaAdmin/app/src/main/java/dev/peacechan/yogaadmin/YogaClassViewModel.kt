package dev.peacechan.yogaadmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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

    //for firebase
    private val firebaseDatabase =
        FirebaseDatabase.getInstance("https://yogaadmin-2024-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val yogaClassesRef = firebaseDatabase.getReference("yoga_classes")

    // StateFlow to hold alert messages
    private val _alertMessage = MutableStateFlow<String?>(null)
    val alertMessage: StateFlow<String?> get() = _alertMessage

    // Sync SQLite to Firebase
    fun syncToFirebase() {
        viewModelScope.launch {
            try {
                // Fetch all yoga classes from SQLite
                val localClasses = repository.getAllYogaClasses()

                // Push each yoga class to Firebase
                for (yogaClass in localClasses) {
                    yogaClassesRef.child(yogaClass.id.toString()).setValue(yogaClass)
                }

                // Show success message
                _alertMessage.value = "Data synced successfully to Firebase."
            } catch (e: Exception) {
                // Show error message
                _alertMessage.value = "Error syncing to Firebase: ${e.message}"
            }
        }
    }

    // Reset Database
    fun resetDatabase() {
        viewModelScope.launch {
            try {
                // Reset the local database
                repository.resetDatabase()

                // Clear the Firebase database
                firebaseDatabase.getReference("yoga_classes")
                    .removeValue()
                    .addOnSuccessListener {
                        _alertMessage.value = "Database reset successfully."
                    }
                    .addOnFailureListener { e ->
                        _alertMessage.value = "Failed to reset Firebase database: ${e.message}"
                    }

                // Reload the local database
                loadYogaClasses()
            } catch (e: Exception) {
                // Show error message
                _alertMessage.value = "Error resetting database: ${e.message}"
            }
        }
    }

    // Clear alert message after dismissing the dialog
    fun clearAlertMessage() {
        _alertMessage.value = null
    }


}

