package com.example.pictureoftheday.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pictureoftheday.PODApp
import com.example.pictureoftheday.R
import com.example.pictureoftheday.view.solarsystem.Planet
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

fun makeList(): MutableList<Pair<Planet, Boolean>> {
    return (listOf(
        Pair(Planet(id=0, type = Planet.TYPE_SUN, img = R.drawable.sun, name = "Солнце", description = "Со́лнце (астр. ☉) — одна из звёзд нашей Галактики (Млечный Путь) и единственная звезда Солнечной системы. Вокруг Солнца обращаются другие объекты этой системы: планеты и их спутники, карликовые планеты и их спутники, астероиды, метеороиды, кометы и космическая пыль."), false),
        Pair(Planet(id=1, type = Planet.TYPE_PLANET, img = R.drawable.mercury, name = "Меркурий", description ="Наименьшая планета Солнечной системы и самая близкая к Солнцу. Названа в честь древнеримского бога торговли - быстрого Меркурия, поскольку она движется по небу быстрее других планет. Её период обращения вокруг Солнца составляет всего 87,97 земных суток - самый короткий среди всех планет Солнечной системы. "), false),
        Pair(Planet(id=2, type = Planet.TYPE_PLANET, img = R.drawable.venus, name = "Венера", description ="Вторая по удалённости от Солнца и шестая по размеру планета Солнечной системы, наряду с Меркурием, Землёй и Марсом принадлежащая к семейству планет земной группы. Названа в честь древнеримской богини любви Венеры."), false),
        Pair(Planet(id=3, type = Planet.TYPE_EARTH, img = R.drawable.earth, name = "Земля", description ="Самая плотная, пятая по диаметру и массе среди всех планет Солнечной системы и крупнейшая среди планет земной группы, в которую входят также Меркурий, Венера и Марс. Единственное известное человеку в настоящее время тело во Вселенной, населённое живыми организмами. "), false),
        Pair(Planet(id=4, type = Planet.TYPE_PLANET, img = R.drawable.mars, name = "Марс", description ="Седьмая по размеру планета Солнечной системы; масса планеты составляет 10,7% массы Земли. Названа в честь Марса - древнеримского бога войны, соответствующего древнегреческому Аресу. Также Марс называют «красной планетой» из-за красноватого оттенка поверхности, придаваемого ей минералом маггемитом - γ-оксидом железа. "), false),
        Pair(Planet(id=5, type = Planet.TYPE_PLANET, img = R.drawable.jupiter, name = "Юпитер", description ="Крупнейшая планета Солнечной системы, пятая по удалённости от Солнца. Наряду с Сатурном, Юпитер классифицируется как газовый гигант."), false),
        Pair(Planet(id=6, type = Planet.TYPE_PLANET, img = R.drawable.saturn, name = "Сатурн", description ="Вторая по размерам планета в Солнечной системе после Юпитера. Сатурн классифицируется как газовая планета-гигант. Сатурн назван в честь римского бога земледелия."), false),
        Pair(Planet(id=7, type = Planet.TYPE_PLANET, img = R.drawable.uran, name = "Уран", description ="Планета Солнечной системы, третья по диаметру и четвёртая по массе. Была открыта в 1781 году английским астрономом Уильямом Гершелем и названа в честь греческого бога неба Урана"), false),
        Pair(Planet(id=8, type = Planet.TYPE_PLANET, img = R.drawable.neptun, name = "Нептун", description ="Восьмая планета Солнечной системы. Его масса превышает массу Земли в 17,2 раза и является третьей среди планет Солнечной системы, а по экваториальному диаметру Нептун занимает четвёртое место, превосходя Землю в 3,9 раза. Планета названа в честь Нептуна - римского бога морей."), false),
    ) as MutableList<Pair<Planet, Boolean>>).apply { shuffle() }
}
