package com.azhara.academy.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.azhara.academy.data.source.remote.RemoteDataSource
import com.azhara.academy.utils.DataDummy
import com.azhara.academy.utils.LiveDataTestUtil
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class AcademyRepositoryTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val academyRepository = FakeAcademyRepositoryTest(remote)

    private val courseResponse = DataDummy.generateRemoteDummyCourses()
    private val courseId = courseResponse[0].id
    private val moduleResponse = DataDummy.generateRemoteDummyModules(courseId)
    private val moduleId = moduleResponse[0].moduleId
    private val content = DataDummy.generateRemoteDummyContent(moduleId)

    @Test
    fun getAllCourses(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadCoursesCallBack)
                .onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourse(any())
        val courseEntities = LiveDataTestUtil.getValue(academyRepository.getAllCourse())
        verify(remote).getAllCourse(any())
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getAllModulebyCourse(){
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadModuleCallBack).onAllModuleReceived(moduleResponse)
            null
        }.`when`(remote).getModules(eq(courseId), any())
        val moduleEntities = LiveDataTestUtil.getValue(academyRepository.getAllModulebyCourse(courseId))
        verify(remote).getModules(eq(courseId), any())
        assertNotNull(moduleEntities)
        assertEquals(moduleResponse.size.toLong(), moduleEntities.size.toLong())
    }

    @Test
    fun getBookbarkedCourse(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadCoursesCallBack).onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourse(any())
        val courseEntities = LiveDataTestUtil.getValue(academyRepository.getAllCourse())
        verify(remote).getAllCourse(any())
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getCourseWithModules(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadCoursesCallBack).onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourse(any())
        val resultCourse = LiveDataTestUtil.getValue(academyRepository.getCourseWithModules(courseId))
        verify(remote).getAllCourse(any())
        assertNotNull(resultCourse)
        assertEquals(courseResponse[0].title, resultCourse.title)
    }

    @Test
    fun getContent(){
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadModuleCallBack).onAllModuleReceived(moduleResponse)
            null
        }.`when`(remote).getModules(eq(courseId), any())
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadContentCallBack).onContentReceived(content)
            null
        }.`when`(remote).getContent(eq(moduleId), any())
        val resultModule = LiveDataTestUtil.getValue(academyRepository.getContent(courseId, moduleId))
        verify(remote).getModules(eq(courseId), any())
        verify(remote).getContent(eq(moduleId), any())
        assertNotNull(resultModule)
        assertEquals(content.content, resultModule.contentEntity?.content)
    }

}