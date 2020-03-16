package com.azhara.academy.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "RESOURCE"
    val espressoTestIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment(){
        espressoTestIdlingResource.increment()
    }

    fun decrement(){
        espressoTestIdlingResource.decrement()
    }
}