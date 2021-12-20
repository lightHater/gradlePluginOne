package com.example.textlinkdemo

import kotlin.reflect.KProperty

class Delegate {
    var value: Any? = null
    operator fun getValue(testKt: Any, property: KProperty<*>): Any? {
        return value
    }

    operator fun setValue(proxyTestKt: Any, property: KProperty<*>, any: Any?) {
        value = any
    }

}