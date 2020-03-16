package com.azhara.academy.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.data.source.local.entity.CourseWithModule
import com.azhara.academy.vo.Resource

class DetailCourseViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    val courseId = MutableLiveData<String>()

    fun setSelectedCourse(id: String){
        this.courseId.value = id
    }

    var courseModule: LiveData<Resource<CourseWithModule>> = Transformations.switchMap(courseId){mCourseId ->
        academyRepository.getCourseWithModules(mCourseId)
    }

    fun setBookmark(){
        val moduleResource = courseModule.value

        if (moduleResource != null){
            val courseWithModule = moduleResource.data

            if (courseWithModule != null){
                val courseEntity = courseWithModule.mCourse
                val newState = !courseEntity.bookmarked
                academyRepository.setCourseBookmark(courseEntity, newState)
            }
        }
    }

}