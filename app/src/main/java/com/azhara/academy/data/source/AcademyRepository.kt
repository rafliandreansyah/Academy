package com.azhara.academy.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.azhara.academy.data.NetworkBoundResource
import com.azhara.academy.data.source.local.LocalDataSource
import com.azhara.academy.data.source.local.entity.ContentEntity
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.data.source.local.entity.CourseWithModule
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.data.source.remote.ApiResponse
import com.azhara.academy.data.source.remote.RemoteDataSource
import com.azhara.academy.data.source.remote.response.ContentResponse
import com.azhara.academy.data.source.remote.response.CourseResponse
import com.azhara.academy.data.source.remote.response.ModuleResponse
import com.azhara.academy.utils.AppExecutors
import com.azhara.academy.vo.Resource

class AcademyRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): AcademyDataSource{


    companion object{
        @Volatile
        private var instance: AcademyRepository? = null

        fun getInstance(remoteData: RemoteDataSource, localData: LocalDataSource, appExecutor: AppExecutors): AcademyRepository =
            instance ?: synchronized(this){
                instance ?: AcademyRepository(remoteData, localData, appExecutor)
            }
    }

    override fun getAllCourse(): LiveData<Resource<List<CourseEntity>>>  {
        return object : NetworkBoundResource<List<CourseEntity>, List<CourseResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<List<CourseEntity>> =
                localDataSource.getAllCourse()


            override fun shouldFetch(data: List<CourseEntity>?): Boolean = data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<CourseResponse>>> = remoteDataSource.getAllCourse()

            override fun saveCallResult(data: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in data){
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

                localDataSource.insertCourse(courseList)
            }

        }.asLiveData()

    }

    override fun getBookbarkedCourse(): LiveData<List<CourseEntity>> {
        return localDataSource.getBookmarkedCourse()
    }

    override fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModule>> {
        return object : NetworkBoundResource<CourseWithModule, List<ModuleResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<CourseWithModule> = localDataSource.getCourseWithModules(courseId)

            override fun shouldFetch(data: CourseWithModule?): Boolean = data?.mModules == null || data.mModules.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> = remoteDataSource.getModules(courseId)

            override fun saveCallResult(data: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in data){
                    val course = ModuleEntity(
                        response.moduleId, response.courseId, response.title, response.position, false
                    )
                    moduleList.add(course)
                }
                localDataSource.insertModule(moduleList)
            }

        }.asLiveData()
    }

    override fun getAllModulebyCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>> {

        return object : NetworkBoundResource<List<ModuleEntity>, List<ModuleResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<List<ModuleEntity>> = localDataSource.getModulesByCourse(courseId)

            override fun shouldFetch(modules: List<ModuleEntity>?): Boolean = modules == null || modules.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> = remoteDataSource.getModules(courseId)

            override fun saveCallResult(data: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in data){
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
                localDataSource.insertModule(moduleList)
            }

        }.asLiveData()

    }

    override fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>> {

        return object : NetworkBoundResource<ModuleEntity, ContentResponse>(appExecutors){
            override fun loadFromDB(): LiveData<ModuleEntity> = localDataSource.getModuleWithContent(moduleId)

            override fun shouldFetch(moduleEntity: ModuleEntity?): Boolean = moduleEntity?.contentEntity == null

            override fun createCall(): LiveData<ApiResponse<ContentResponse>> = remoteDataSource.getContent(moduleId)

            override fun saveCallResult(contentResponse: ContentResponse) = localDataSource.updateContent(contentResponse.content, moduleId)

        }.asLiveData()


    }

    override fun setCourseBookmark(course: CourseEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setCourseBookmark(course, state) }

    override fun setReadModule(module: ModuleEntity) =
        appExecutors.diskIO().execute { localDataSource.setReadModule(module) }



}