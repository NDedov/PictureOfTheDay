package com.example.pictureoftheday.model.retrofit

import android.annotation.SuppressLint
import android.widget.Toast
import com.example.pictureoftheday.BuildConfig
import com.example.pictureoftheday.domain.PODServerResponseData
import com.example.pictureoftheday.domain.convertServerResponseDataToPODData
import com.example.pictureoftheday.model.CommonPODCallback
import com.example.pictureoftheday.model.RepositoryPODByDate
import com.example.pictureoftheday.utils.getNASADate
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class RepositoryPODRetrofitImpl : RepositoryPODByDate {
    @SuppressLint("SimpleDateFormat")
    override fun getPOD(date: Date, callback: CommonPODCallback) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl("https://api.nasa.gov/")
        retrofitImpl.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        val api = retrofitImpl.build().create(PictureOfTheDayAPI::class.java)

        api.getPictureOfTheDayByDate(
            BuildConfig.NASA_API_KEY,
            getNASADate(date)
        )
            .enqueue(object : Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(convertServerResponseDataToPODData(response.body()!!))
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            callback.onError("Unidentified error")
                        } else {
                            callback.onError(message.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    callback.onFailure(t)
                }
            })
    }
}
