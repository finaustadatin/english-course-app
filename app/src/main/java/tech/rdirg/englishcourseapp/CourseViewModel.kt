package tech.rdirg.englishcourseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.rdirg.englishcourseapp.model.Course
import tech.rdirg.englishcourseapp.repository.CourseRepository

class CourseViewModel(private val repository: CourseRepository): ViewModel() {
    val allCourses: LiveData<List<Course>> = repository.allCourses.asLiveData()

    fun insert(course: Course) = viewModelScope.launch {
        repository.insert(course)
    }

    fun delete(course: Course) = viewModelScope.launch {
        repository.delete(course)
    }

    fun update(course: Course) = viewModelScope.launch {
        repository.update(course)
    }
}

class CourseViewModelFactory(private val repository: CourseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
