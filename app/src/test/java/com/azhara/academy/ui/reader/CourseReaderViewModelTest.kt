package com.azhara.academy.ui.reader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.data.source.local.entity.ContentEntity
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
class CourseReaderViewModelTest {

    private lateinit var viewModel: CourseReaderViewModel

    private val dummyCourse = DataDummy.generateDummyCourse()[0]
    private val courseId = dummyCourse.courseId
    private val dummyModul = DataDummy.generateDummyModules(courseId)
    private val moduleId  = dummyModul[0].moduleId

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Mock
    private lateinit var modulesObserver: Observer<List<ModuleEntity>>

    @Mock
    private lateinit var moduleObserver: Observer<ModuleEntity>

    @Before
    fun setUp() {
        viewModel = CourseReaderViewModel(academyRepository)
        viewModel.setSelectedCourse(courseId)
        viewModel.setSelectedModule(moduleId)

        val dummyModule = dummyModul[0]

        dummyModule.contentEntity =
            ContentEntity("<h3 class=\\\\\\\"fr-text-bordered\\\\\\\">" + dummyModule.title + "</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>")
    }

    @Test
    fun getModules() {
        val modules = MutableLiveData<ArrayList<ModuleEntity>>()
        modules.value = dummyModul

        `when`(academyRepository.getAllModulebyCourse(courseId)).thenReturn(modules)
        val moduleEntities = viewModel.getModules().value
        verify(academyRepository).getAllModulebyCourse(courseId)
        assertNotNull(moduleEntities)
        assertEquals(7, moduleEntities?.size)

        viewModel.getModules().observeForever(modulesObserver)
        verify(modulesObserver).onChanged(dummyModul)
    }

    @Test
    fun getSelectedModule() {
        val module = MutableLiveData<ModuleEntity>()
        module.value = dummyModul[0]

        `when`(academyRepository.getContent(courseId, moduleId)).thenReturn(module)
        val moduleEntity = viewModel.getSelectedModule().value as ModuleEntity
        verify(academyRepository).getContent(courseId, moduleId)
        assertNotNull(moduleEntity)
        val contentEntity = moduleEntity.contentEntity
        val content = contentEntity?.content
        assertNotNull(content)
        assertEquals(content, dummyModul[0].contentEntity?.content)

        viewModel.getSelectedModule().observeForever(moduleObserver)
        verify(moduleObserver).onChanged(dummyModul[0])
    }
}