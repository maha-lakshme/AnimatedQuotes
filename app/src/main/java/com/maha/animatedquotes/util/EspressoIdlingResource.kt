package com.maha.animatedquotes.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE= "GLOBAL"
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment(){
        // Increment the counting resource
        countingIdlingResource.increment()
    }

    fun decrement(){
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}