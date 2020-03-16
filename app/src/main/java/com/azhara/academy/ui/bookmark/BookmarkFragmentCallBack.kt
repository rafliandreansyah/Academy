package com.azhara.academy.ui.bookmark

import com.azhara.academy.data.source.local.entity.CourseEntity

interface BookmarkFragmentCallBack {
    fun onShareClick(course: CourseEntity)
}