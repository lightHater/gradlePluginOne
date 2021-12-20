package com.example.textlinkdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

private const val TAG = "ProxyTestKt"
class ProxyTestKt {

    private val patch by lazy {
        "Student"
    }
    private val dd by Delegate()

    fun <T> proxyTest(service: Class<T>): T {
        return Proxy.newProxyInstance(
                service.classLoader, arrayOf<Class<*>>(service),
                object : InvocationHandler {
                    @Throws(Throwable::class)
                    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
                        Log.d(TAG, "invoke method = $method args = $args")
                        // If the method is a method from Object then defer to normal invocation.
                        return if (method.declaringClass == Any::class.java) {
                            testDots(null)
                            Log.d(TAG, "after testDots")
                            ProxyTest.testDots(args)
                            Log.d(TAG, "after testDots args")
//                            method.invoke(this, args)
                            Log.d(TAG, "after invoke")
                        } else null
                    }
                }) as T
    }

    fun testDotsDirectly() {
        ProxyTest.testDots(null)

        var count = 0
        runBlocking {
            val mutex = Mutex()
            repeat(1000) {
                launch {
                    mutex.withLock {
                        count++
                    }
                }
            }
        }
        Log.d(TAG, "testDotsDirectly: $count")
    }

    fun testDots(vararg args: Any?) {
        Log.d(TAG, "testDots " + if (args == null) null else args.size)
    }
}

fun testUser(context: Context) {
    val user = TestUser("uuu", "mm")
    MyLinearLayout(context, null)
}

inline fun <reified T: Activity> Activity.startMyActivity(x: Int, block: Intent.(Int) -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block(x + 5)
    Log.d("ProxyTestKt", "shiHua: ${intent.extras}")
    this.startActivity(intent)
}