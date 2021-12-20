package com.example.textlinkdemo

import android.util.Log

class FanyiResponse {
    var translateResult: MutableList<MutableList<TranslateResulttt>>? = null
    var type: String? = null
    var errorCode:Int? = 0


    class TranslateResulttt {
        var src:String? = null
        var tgt:String? = null
    }
}