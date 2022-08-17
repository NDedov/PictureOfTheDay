package com.example.pictureoftheday.viewmodel

import com.example.pictureoftheday.domain.PODServerResponseData

sealed class PictureOfTheDayAppState {
    data class Success(val serverResponseData: PODServerResponseData) : PictureOfTheDayAppState()
    data class Error(val error: Throwable) : PictureOfTheDayAppState()
    data class Loading(val progress: Int?) : PictureOfTheDayAppState()
}