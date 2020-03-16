package com.azhara.academy.data.source.local

import androidx.lifecycle.LiveData
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.CourseWithModule
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.data.source.local.room.AcademyDao

class LocalDataSource private constructor(private val mAcademyDao: AcademyDao){

    companion object{
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(academyDao: AcademyDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(academyDao)
    }

    fun getAllCourse(): LiveData<List<CourseEntity>> = mAcademyDao.getAllCourse()

    fun getBookmarkedCourse(): LiveData<List<CourseEntity>> = mAcademyDao.getBookmarkedCourse()

    fun getCourseWithModules(courseId: String): LiveData<CourseWithModule> =
        mAcademyDao.getCourseWithModuleById(courseId)

    fun getModulesByCourse(courseId: String): LiveData<List<ModuleEntity>> =
        mAcademyDao.getModuleByCourseId(courseId)

    fun insertCourse(courses: List<CourseEntity>) = mAcademyDao.insertCourses(courses)

    fun insertModule(module: List<ModuleEntity>) = mAcademyDao.insertModule(module)

    fun setCourseBookmark(course: CourseEntity, newState: Boolean) {
        course.bookmarked = newState
        mAcademyDao.updateCourse(course)
    }

    fun getModuleWithContent(moduleId: String): LiveData<ModuleEntity> = mAcademyDao.getModuleById(moduleId)

    fun updateContent(content: String, moduleId: String){
        mAcademyDao.updateModuleByContent(content, moduleId)
    }

    fun setReadModule(module: ModuleEntity){
        module.read = true
        mAcademyDao.updateModule(module)
    }

}