package tech.rdirg.englishcourseapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import tech.rdirg.englishcourseapp.dao.CourseDao
import tech.rdirg.englishcourseapp.model.Course

@Database(entities = [Course::class], version = 2, exportSchema = false)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        private var INSTANCE:CourseDatabase? = null

        private val migration1To2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE course_table ADD COLUMN longitude REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE course_table ADD COLUMN latitude REAL NOT NULL DEFAULT 0.0")
            }
        }

        fun getDatabase(contex: Context): CourseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    contex.applicationContext,
                    CourseDatabase::class.java,
                    "course_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}