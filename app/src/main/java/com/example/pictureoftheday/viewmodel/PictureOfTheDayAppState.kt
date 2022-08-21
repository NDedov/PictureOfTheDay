package com.example.pictureoftheday.viewmodel

import com.example.pictureoftheday.model.domain.PODData

sealed class PictureOfTheDayAppState {
    data class Success(val serverResponseData: PODData) : PictureOfTheDayAppState()
    data class Error(val error: Throwable) : PictureOfTheDayAppState()
    data class Loading(val progress: Int?) : PictureOfTheDayAppState()
}