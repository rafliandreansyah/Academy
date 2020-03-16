package com.azhara.academy.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.ModuleEntity
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
class DetailCourseViewModelTest {

    private lateinit var viewModel: DetailCourseViewModel

    private val dummyCourse = DataDummy.generateDummyCourse()[0]
    private val courseId = dummyCourse.courseId
    private val dummyModule = DataDummy.generateDummyModules(courseId)

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Mock
    private lateinit var courseObserver: Observer<CourseEntity>

    @Mock
    private lateinit var moduleObserver: Observer<List<ModuleEntity>>

    @Before
    fun setUp(){
        viewModel = DetailCourseViewModel(academyRepository)
        viewModel.setSelectedCourse(courseId)
    }

    @Test
    fun getCourse() {
        val course = MutableLiveData<CourseEntity>()
        course.value = dummyCourse

        `when`(academyRepository.getCourseWithModules(courseId)).thenReturn(course)
        val courseEntity = viewModel.getCourse().value as CourseEntity
        verify(academyRepository).getCourseWithModules(courseId)
        assertNotNull(courseEntity)
        assertEquals(dummyCourse.courseId, courseEntity.courseId)
        assertEquals(dummyCourse.imagePath, courseEntity.imagePath)
        assertEquals(dummyCourse.deadline, courseEntity.deadline)
        assertEquals(dummyCourse.title, courseEntity.title)
        assertEquals(dummyCourse.description, courseEntity.description)

        viewModel.getCourse().observeForever(courseObserver)
        verify(courseObserver).onChanged(dummyCourse)
    }

    @Test
    fun getModules() {
        val module = MutableLiveData<ArrayList<ModuleEntity>>()
        module.value = dummyModule

        `when`(academyRepository.getAllModulebyCourse(courseId)).thenReturn(module)
        val moduleEntity = viewModel.getModules().value
        verify(academyRepository).getAllModulebyCourse(courseId)
        assertNotNull(moduleEntity)
        assertEquals(7, moduleEntity?.size)

        viewModel.getModules().observeForever(moduleObserver)
        verify(moduleObserver).onChanged(dummyModule)
    }
}