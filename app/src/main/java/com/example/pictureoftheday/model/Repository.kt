package com.example.pictureoftheday.model

import com.example.pictureoftheday.domain.PODData
import com.example.pictureoftheday.domain.PODServerResponseData
import java.util.*

interface CommonPODCallback{
    fun onResponse(data: PODData)
    fun onFailure(t: Throwable)
    fun onError (message: String)
}

fun interface RepositoryPODByDate {
    fun getPOD(date: Date, callback: CommonPODCallback)
}