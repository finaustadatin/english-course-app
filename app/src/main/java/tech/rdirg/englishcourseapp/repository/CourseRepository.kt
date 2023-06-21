package tech.rdirg.englishcourseapp.repository;

import kotlinx.coroutines.flow.Flow;
import tech.rdirg.englishcourseapp.dao.CourseDao;
import tech.rdirg.englishcourseapp.model.Course;

class CourseRepository(private val courseDao:CourseDao) {
    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun insert(course: Course) {
        courseDao.insertCourse(course)
    }

    suspend fun delete(course: Course) {
        courseDao.deleteCourse(course)
    }

    suspend fun update(course: Course) {
        courseDao.updateCourse(course)
    }
}
