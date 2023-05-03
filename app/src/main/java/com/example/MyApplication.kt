package com.example

import android.app.Application
import com.kdownloader.KDownloader

class MyApplication : Application() {

    lateinit var kDownloader: KDownloader

    override fun onCreate() {
        super.onCreate()
        kDownloader = KDownloader.create(applicationContext)
    }

}