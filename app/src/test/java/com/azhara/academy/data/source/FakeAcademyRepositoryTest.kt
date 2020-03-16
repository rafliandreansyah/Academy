package com.azhara.academy.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.azhara.academy.data.source.local.entity.ContentEntity
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.data.source.remote.RemoteDataSource
import com.azhara.academy.data.source.remote.response.ContentResponse
import com.azhara.academy.data.source.remote.response.CourseResponse
import com.azhara.academy.data.source.remote.response.ModuleResponse
import org.junit.Assert.*

class FakeAcademyRepositoryTest(private val remoteDataSource: RemoteDataSource): AcademyDataSource{

    override fun getAllCourse(): LiveData<List<CourseEntity>> {
        val coursesResult = MutableLiveData<List<CourseEntity>>()
        remoteDataSource.getAllCourse(object : RemoteDataSource.LoadCoursesCallBack{
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()

                for (response in courseResponse){
                    val course =
                        CourseEntity(
                            response.id,
                            response.title,
                            response.description,
                            response.date,
                            false,
                            response.imgPath
                        )
                    courseList.add(course)
                }
                coursesResult.postValue(courseList)
            }
        })
        return coursesResult

    }

    override fun getBookbarkedCourse(): LiveData<List<CourseEntity>> {
        val courseResult = MutableLiveData<List<CourseEntity>>()
        remoteDataSource.getAllCourse(object : RemoteDataSource.LoadCoursesCallBack{
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()

                for (response in courseResponse){
                    val course =
                        CourseEntity(
                            response.id,
                            response.title,
                            response.description,
                            response.date,
                            false,
                            response.imgPath
                        )

                    courseList.add(course)
                }
                courseResult.postValue(courseList)
            }
        })

        return courseResult
    }

    override fun getCourseWithModules(courseId: String): LiveData<CourseEntity> {
        val courseResult = MutableLiveData<CourseEntity>()
        remoteDataSource.getAllCourse(object : RemoteDataSource.LoadCoursesCallBack{
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                lateinit var course: CourseEntity
                for (response in courseResponse){
                    if (response.id == courseId){
                        course =
                            CourseEntity(
                                response.id,
                                response.title,
                                response.description,
                                response.date,
                                false,
                                response.imgPath)
                    }
                }
                courseResult.postValue(course)
            }
        })
        return courseResult
    }

    override fun getAllModulebyCourse(courseId: String): LiveData<ArrayList<ModuleEntity>> {
        val moduleResult = MutableLiveData<ArrayList<ModuleEntity>>()
        remoteDataSource.getModules(courseId, object: RemoteDataSource.LoadModuleCallBack{
            override fun onAllModuleReceived(moduleResponse: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()

                for (response in moduleResponse){
                    val course =
                        ModuleEntity(
                            response.moduleId,
                            response.courseId,
                            response.title,
                            response.position,
                            false
                        )
                    moduleList.add(course)
                }
                moduleResult.postValue(moduleList)
            }

        })
        return moduleResult

    }


    override fun getContent(courseId: String, moduleId: String): LiveData<ModuleEntity> {
        val moduleResult = MutableLiveData<ModuleEntity>()

        remoteDataSource.getModules(courseId, object : RemoteDataSource.LoadModuleCallBack{
            override fun onAllModuleReceived(moduleResponse: List<ModuleResponse>) {
                lateinit var module: ModuleEntity
                for (response in moduleResponse){
                    if (response.moduleId == moduleId){
                        module =
                            ModuleEntity(
                                response.moduleId,
                                response.courseId,
                                response.title,
                                response.position,
                                false
                            )
                        remoteDataSource.getContent(moduleId, object : RemoteDataSource.LoadContentCallBack{
                            override fun onContentReceived(contentResponse: ContentResponse) {
                                module.contentEntity = ContentEntity(contentResponse.content)
                                moduleResult.postValue(module)
                            }


                        })
                        break
                    }

                }
            }
        })

        return moduleResult
    }

}