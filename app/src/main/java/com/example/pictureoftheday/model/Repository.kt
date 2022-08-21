package com.example.pictureoftheday.model

import com.example.pictureoftheday.model.domain.PODData
import java.util.*

interface CommonPODCallback{
    fun onResponse(data: PODData)
    fun onFailure(t: Throwable)
    fun onError (message: String)
}

fun interface RepositoryPODByDate {
    fun getPOD(date: Date, callback: CommonPODCallback)
}