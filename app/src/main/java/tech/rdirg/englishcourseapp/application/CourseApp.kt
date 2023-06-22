package tech.rdirg.englishcourseapp.application

import android.app.Application
import tech.rdirg.englishcourseapp.repository.CourseRepository

class CourseApp: Application() {
    val database by lazy { CourseDatabase.getDatabase(this) }
    val repository by lazy { CourseRepository(database.courseDao()) }
}