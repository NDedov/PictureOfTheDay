package com.example.pictureoftheday.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureoftheday.PODApp
import com.example.pictureoftheday.domain.PODData
import com.example.pictureoftheday.domain.PODServerResponseData
import com.example.pictureoftheday.model.CommonPODCallback
import com.example.pictureoftheday.model.RepositoryPODByDate
import java.util.*

class PictureOfTheDayViewModel(private val liveData: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData()) :
    ViewModel() {

    private lateinit var repository: RepositoryPODByDate

    fun getLiveData(): MutableLiveData<PictureOfTheDayAppState> {
        repository = PODApp.repositoryPODRetrofitImplCommon
        return liveData
    }

    fun getData(date: Date) {
        liveData.value = PictureOfTheDayAppState.Loading(null)
        repository.getPOD(date, callback)
    }

    private val callback = object : CommonPODCallback {
        override fun onResponse(data: PODData) {
            liveData.postValue(PictureOfTheDayAppState.Success(data))
        }

        override fun onFailure(t: Throwable) {
            liveData.postValue(PictureOfTheDayAppState.Error(t))
        }

        override fun onError(message: String) {
            liveData.postValue(PictureOfTheDayAppState.Error(RuntimeException(message)))
        }
    }
}