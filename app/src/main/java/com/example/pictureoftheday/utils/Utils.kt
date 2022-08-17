package com.example.pictureoftheday.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.PODApp


@RequiresApi(Build.VERSION_CODES.M)
fun isConnection(): Boolean {
    val connectivityManager = PODApp.getMyApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}