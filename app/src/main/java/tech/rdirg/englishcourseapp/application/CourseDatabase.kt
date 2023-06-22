package tech.rdirg.englishcourseapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import tech.rdirg.englishcourseapp.dao.CourseDao
import tech.rdirg.englishcourseapp.model.Course

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        private var INSTANCE:CourseDatabase? = null

        fun getDatabase(contex: Context): CourseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    contex.applicationContext,
                    CourseDatabase::class.java,
                    "course_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}