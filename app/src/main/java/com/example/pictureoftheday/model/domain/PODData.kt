package com.example.pictureoftheday.model.domain

data class PODData (
    val explanation: String?,
    val title: String?,
    val url: String?,
)

fun convertServerResponseDataToPODData(podServerResponseData: PODServerResponseData):PODData{
   return PODData(podServerResponseData.explanation,podServerResponseData.title,podServerResponseData.url)
}