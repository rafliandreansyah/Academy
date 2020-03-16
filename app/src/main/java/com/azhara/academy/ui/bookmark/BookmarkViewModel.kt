package com.azhara.academy.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.AcademyRepository

class BookmarkViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    fun getBookmark(): LiveData<List<CourseEntity>> {
        return academyRepository.getBookbarkedCourse()
    }

}