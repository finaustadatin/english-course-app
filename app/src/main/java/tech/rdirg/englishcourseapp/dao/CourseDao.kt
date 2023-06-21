package tech.rdirg.englishcourseapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.rdirg.englishcourseapp.model.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM course_table ORDER BY id ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Insert
    suspend fun insertCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Update
    fun updateCourse(course: Course)
}