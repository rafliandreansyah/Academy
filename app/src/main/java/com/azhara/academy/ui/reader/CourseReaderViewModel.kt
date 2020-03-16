package com.azhara.academy.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.vo.Resource

class CourseReaderViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    var courseId = MutableLiveData<String>()
    var moduleId = MutableLiveData<String>()

    fun setSelectedCourse(courseId: String){
        this.courseId.value = courseId
    }

    fun setSelectedModule(moduleId: String){
        this.moduleId.value = moduleId
    }

    var module: LiveData<Resource<List<ModuleEntity>>> = Transformations.switchMap(courseId){ mCourseId ->
        academyRepository.getAllModulebyCourse(mCourseId)
    }

    var selectModule: LiveData<Resource<ModuleEntity>> = Transformations.switchMap(moduleId){ mModuleId ->
        academyRepository.getContent(mModuleId)
    }

    fun readContent(module: ModuleEntity){
        academyRepository.setReadModule(module)
    }

    fun getModuleSize(): Int{
        if (module.value != null){
            val moduleEntities = module.value?.data
            if (moduleEntities != null){
                return moduleEntities.size
            }
        }
        return 0
    }

    fun setNextPage(){
        if (selectModule.value != null && module.value != null){
            val moduleEntity = selectModule.value?.data
            val moduleEntities = module.value?.data
            if (moduleEntity != null && moduleEntities != null){
                val position = moduleEntity.position
                if (position < moduleEntities.size && position >= 0){
                    moduleId.value = moduleEntities[position + 1].moduleId
                }
            }
        }
    }

    fun setPrevPage(){
        if (selectModule.value != null && module.value != null){
            val moduleEntity = selectModule.value?.data
            val moduleEntities = module.value?.data
            if (moduleEntity != null && moduleEntities != null){
                val position = moduleEntity.position
                if (position < moduleEntities.size && position >= 0){
                    moduleId.value = moduleEntities[position - 1].moduleId
                }
            }
        }
    }

}