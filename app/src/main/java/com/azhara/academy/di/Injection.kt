package com.azhara.academy.di

import android.content.Context
import com.azhara.academy.data.source.AcademyRepository
import com.azhara.academy.data.source.local.LocalDataSource
import com.azhara.academy.data.source.local.room.AcademyDatabase
import com.azhara.academy.data.source.remote.RemoteDataSource
import com.azhara.academy.utils.AppExecutors
import com.azhara.academy.utils.JsonHelper

object Injection {

    fun providerRepository(context: Context): AcademyRepository{

        val database = AcademyDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localData = LocalDataSource.getInstance(database.academyDao())
        val executors = AppExecutors()

        return AcademyRepository.getInstance(remoteDataSource, localData, executors)
    }

}