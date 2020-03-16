package com.azhara.academy.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    fun getCourse(): LiveData<Resource<List<CourseEntity>>> = academyRepository.getAllCourse()

}