package com.azhara.academy.data.source

import androidx.lifecycle.LiveData
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.CourseWithModule
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.vo.Resource

interface AcademyDataSource {
    fun getAllCourse(): LiveData<Resource<List<CourseEntity>>>

    fun getBookbarkedCourse(): LiveData<List<CourseEntity>>

    fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModule>>

    fun getAllModulebyCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>>

    fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>>

    fun setCourseBookmark(course: CourseEntity, state: Boolean)

    fun setReadModule(module: ModuleEntity)

}