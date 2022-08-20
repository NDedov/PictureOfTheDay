package com.example.pictureoftheday.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.PODApp
import java.text.SimpleDateFormat
import java.util.*


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

@SuppressLint("SimpleDateFormat")
fun getNASADate(date: Date):String{
    val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
    sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
    val now = SimpleDateFormat("yyyy-MM-dd").format(date)
    val parsed = sourceFormat.parse(now)
    val tz = TimeZone.getTimeZone("America/Los_Angeles")
    val destFormat = SimpleDateFormat("yyyy-MM-dd")
    destFormat.timeZone = tz
    return destFormat.format(parsed!!)
}