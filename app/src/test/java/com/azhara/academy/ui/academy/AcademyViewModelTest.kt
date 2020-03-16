package com.azhara.academy.ui.academy

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
class AcademyViewModelTest {

    private lateinit var viewModel: AcademyViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Mock
    private lateinit var observer: Observer<List<CourseEntity>>

    @Before
    fun setUp(){
        viewModel = AcademyViewModel(academyRepository)
    }

    @Test
    fun getCourse() {
        val dummyCourse = DataDummy.generateDummyCourse()
        val course = MutableLiveData<List<CourseEntity>>()
        course.value = dummyCourse

        `when`(academyRepository.getAllCourse()).thenReturn(course)
        val courseEntity = viewModel.getCourse().value
        verify(academyRepository).getAllCourse()
        assertNotNull(courseEntity)
        assertEquals(5, courseEntity?.size)

        viewModel.getCourse().observeForever(observer)
        verify(observer).onChanged(dummyCourse)
    }
}