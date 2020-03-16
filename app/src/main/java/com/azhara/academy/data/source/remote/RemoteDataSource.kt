package com.azhara.academy.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.azhara.academy.data.source.remote.response.ContentResponse
import com.azhara.academy.data.source.remote.response.CourseResponse
import com.azhara.academy.data.source.remote.response.ModuleResponse
import com.azhara.academy.utils.EspressoIdlingResource
import com.azhara.academy.utils.JsonHelper

class RemoteDataSource private constructor(private val jsonHelper: JsonHelper){
    
    private val handler = android.os.Handler()

    companion object{
        private const val SERVICE_LATENCY_IN_MILLIS: Long = 2000

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: JsonHelper): RemoteDataSource =
            instance?: synchronized(this){
                instance ?: RemoteDataSource(helper)
            }
    }

    fun getAllCourse(): LiveData<ApiResponse<List<CourseResponse>>> {
        EspressoIdlingResource.increment()
        val resultCourse = MutableLiveData<ApiResponse<List<CourseResponse>>>()
        handler.postDelayed({
            resultCourse.value = ApiResponse.success(jsonHelper.loadCourse())
            EspressoIdlingResource.decrement()
        }, SERVICE_LATENCY_IN_MILLIS)
        return resultCourse
    }
    fun getModules(courseId: String): LiveData<ApiResponse<List<ModuleResponse>>> {
        EspressoIdlingResource.increment()
        val resultModule = MutableLiveData<ApiResponse<List<ModuleResponse>>>()
        handler.postDelayed({
            resultModule.value = ApiResponse.success(jsonHelper.loadModule(courseId))
            EspressoIdlingResource.decrement()
        }, SERVICE_LATENCY_IN_MILLIS)
        return resultModule
    }
    fun getContent(moduleId: String): LiveData<ApiResponse<ContentResponse>> {
        EspressoIdlingResource.increment()
        val resultContent = MutableLiveData<ApiResponse<ContentResponse>>()
        handler.postDelayed({
            resultContent.value = ApiResponse.success(jsonHelper.loadContent(moduleId))
            EspressoIdlingResource.decrement()
        }, SERVICE_LATENCY_IN_MILLIS)
        return resultContent
    }

//    interface LoadCoursesCallBack{
//        fun onAllCoursesReceived(courseResponse: List<CourseResponse>)
//    }
//
//    interface LoadModuleCallBack{
//        fun onAllModuleReceived(moduleResponse: List<ModuleResponse>)
//    }
//
//    interface LoadContentCallBack{
//        fun onContentReceived(contentResponse: ContentResponse)
//    }

}