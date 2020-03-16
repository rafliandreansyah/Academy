package com.azhara.academy.ui.bookmark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.utils.DataDummy
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookmarkViewModelTest {

    private lateinit var bookmarkViewModel: BookmarkViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Mock
    private lateinit var observer: Observer<List<CourseEntity>>

    @Before
    fun setUp(){
        bookmarkViewModel = BookmarkViewModel(academyRepository)
    }

    @Test
    fun getBookbark() {
        val dummyCourse = DataDummy.generateDummyCourse()
        val courses = MutableLiveData<List<CourseEntity>>()
        courses.value = dummyCourse

        `when`(academyRepository.getBookbarkedCourse()).thenReturn(courses)
        val courseEntity = bookmarkViewModel.getBookmark().value
        verify(academyRepository).getBookbarkedCourse()
        assertNotNull(courseEntity)
        assertEquals(5, courseEntity?.size)

        bookmarkViewModel.getBookmark().observeForever(observer)
        verify(observer).onChanged(dummyCourse)
    }
}