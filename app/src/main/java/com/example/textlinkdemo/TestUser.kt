package com.example.textlinkdemo

import android.util.Log

private const val TAG = "TestUser"
class TestUser(val name: String, var ss: String, hair: Int? = 0) {

    init {
        Log.d(TAG, ": $name $ss $hair")
    }

    var mName: String? = name
    var login: String? = null
    var bio: String? = null // 字段名必须和服务器的字段名相同
    var bioo: String? = null //服务器没有此字段，拿不到

    fun testPublicInvocation() {
        mName = ss
        ss = "dd"
        Log.d("wow", "testPublicInvocation")
    }

    fun testPrivateInvocation() {
        Log.d("wow", "testPrivateInvocation")
    }
}