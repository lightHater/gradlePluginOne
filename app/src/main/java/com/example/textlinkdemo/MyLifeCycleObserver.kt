package com.example.textlinkdemo

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

private const val TAG = "MyLifeCycleObserver"
class MyLifeCycleObserver: LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        Log.d(TAG, "resume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        Log.d(TAG, "pause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun create() = Log.d(TAG, "create: ")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() = Log.d(TAG, "destroy: ")
}