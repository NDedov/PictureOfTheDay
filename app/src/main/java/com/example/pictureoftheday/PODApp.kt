package com.example.pictureoftheday

import android.app.Application
import com.example.pictureoftheday.model.retrofit.RepositoryPODRetrofitImpl

class PODApp : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this
    }
    companion object {
        val repositoryPODRetrofitImplCommon = RepositoryPODRetrofitImpl()
        private var myApp: PODApp? = null
        fun getMyApp() = myApp!!
    }
}
