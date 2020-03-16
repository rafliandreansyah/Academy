package com.azhara.academy.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.CourseWithModule
import com.azhara.academy.data.source.local.entity.ModuleEntity

@Dao
interface AcademyDao {

    @Query("SELECT * FROM courseentity")
    fun getAllCourse(): LiveData<List<CourseEntity>>

    @Query("SELECT * FROM courseentity where bookmarked = 1")
    fun getBookmarkedCourse(): LiveData<List<CourseEntity>>

    @Transaction
    @Query("SELECT * FROM courseentity WHERE courseId = :courseId")
    fun getCourseWithModuleById(courseId: String): LiveData<CourseWithModule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(course: List<CourseEntity>)

    @Update
    fun updateCourse(course: CourseEntity)

    @Query("SELECT * FROM moduleentity WHERE courseId = :courseId")
    fun getModuleByCourseId(courseId: String): LiveData<List<ModuleEntity>>

    @Query("SELECT * FROM moduleentity WHERE moduleId = :moduleId")
    fun getModuleById(moduleId: String): LiveData<ModuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModule(module: List<ModuleEntity>)

    @Update
    fun updateModule(module: ModuleEntity)

    @Query("UPDATE moduleentity SET content = :content WHERE moduleId = :moduleId")
    fun updateModuleByContent(content: String, moduleId: String)

}