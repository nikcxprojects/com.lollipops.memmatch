package com.lollipops.memmatch

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class App : Application()  {
    private val YANDEX_API_KEY = "f6a18a54-467f-4a50-9aa0-862ab7822e74"

    companion object{
        private lateinit var myApp: App
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
        val config = YandexMetricaConfig.newConfigBuilder(YANDEX_API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }

}