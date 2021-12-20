package com.example.textlinkdemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner

private const val TAG = "MyApplication"
class MyApplication: Application() {

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()

//        ProcessLifecycleOwner.get().lifecycle.addObserver(MyLifeCycleObserver())
    }
}