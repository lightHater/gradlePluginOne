package com.example.textlinkdemo

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.observers.Subscribers
import rx.schedulers.Schedulers
import java.io.IOException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.net.URL
import java.util.concurrent.CountDownLatch

private const val TAG = "Main2ViewModel"

class Main2ViewModel(private val context: Application) : AndroidViewModel(context) {
    val text: MutableLiveData<String> = MutableLiveData("text")
    val imageUrl: MutableLiveData<String> = MutableLiveData()
    val defaultRes:MutableLiveData<Int> = MutableLiveData(R.drawable.ic_launcher_background)
    val lifeLiveDataTest: MutableLiveData<String> = MutableLiveData()

    fun xiecheng1Click() {
//        testCoroutines()
        Log.d(TAG, "xiecheng1Click: $context")
        loadDirectly()
    }

    val directlyLoadJob = SupervisorJob()
    val directlyLoadScope = CoroutineScope(Dispatchers.Main + directlyLoadJob)

    @Throws(IOException::class)
    private fun loadDirectly() {
        directlyLoadScope.launch {
            Log.d(TAG, "loadDirectly launch")
            val result = mockInternet()
            Log.d(TAG, "loadDirectly after mock")
            text.value = result
        }
    }

    private fun testCoroutines() {
        runBlocking {
            Log.d(TAG, "block current thread until coroutines completion")
            launch {  //主线程;若runBlocking指定IO，则此处为另一子线程，应是也用了IO
                Log.d(TAG, "in launch")
            }
        }

        GlobalScope.launch {
            Log.d(TAG, "global launch")
        }

        val result1 = GlobalScope.async {
            Log.d(TAG, "in async")
            6
        }
        val result2 = GlobalScope.async {
            Log.d(TAG, "async 2")
            5
        }
        GlobalScope.launch(Dispatchers.IO) {
            for (i in 0 until 1) {
                Log.d(TAG, "current thread before launch")
                launch {
                    Log.d(TAG, "launch $i")
                    delay(1000)
                    Log.d(TAG, "launch after delay")
                }
                launch {
                    Log.d(TAG, "launch 1")
                    Log.d(TAG, "launch 1 ${result1.await() + result2.await()}")
                }
            }
            testCrazyCreateCo()
            Log.d(TAG, "after create")
//            testSleep()
        }
    }

    private suspend fun testSleep() {
        Log.d(TAG, "testSleep")
        Thread.sleep(5000) //不切线程，只是sleep当前协程所在的线程
        Log.d(TAG, "testSleep after sleep")
    }

    private suspend fun testCrazyCreateCo() {
        Log.d(TAG, "testCrazyCreateCo start")
        val start = SystemClock.elapsedRealtime()
        val times = 10 * 10000
        val count = CountDownLatch(times)
        val result = GlobalScope.async {
            launch {
                repeat(times) {
                    count.countDown()
                }
            }
        }
        /*
        * withContext 一调用立刻开始执行；阻塞当前协程直到内部的代码和子协程执行完毕；最后一行代码为返回值
        * 同 async{}.await(), async也是一调用立刻执行；await阻塞直到async执行完；await返回async的最后一行
        * */
        /*withContext(Dispatchers.IO) {
            *//*for (i in 1 .. times) {
                launch {
                    count.countDown()
                }
            }*//*
            5
        }*/
        // 创建100000个协程 2s多。start和此处不是同一线程，因为await挂起了，切回来时换了线程;await阻塞当前协程,直到async执行完
        Log.d(TAG, "testCrazyCreateCo times $times count ${result.await()} ${count.count} + time ${SystemClock.elapsedRealtime() - start}")
//        Log.d(TAG, "testCrazyCreateCo withContext count ${count.count} time ${SystemClock.elapsedRealtime() - start}")
        delay(50)
        Log.d(TAG, "after await ${count.count}")
    }

    var i: Int = 0
    fun glideLoad() {
        imageUrl.value = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Ffzn.cc%2Fwp-content%2Fuploads%2F2015%2F12%2F20780130d0a707bee5dbdd0c6e9c4f11.gif&refer=http%3A%2F%2Ffzn.cc&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625062523&t=d3c7595b82dff4ebac1783821eace177"
        text.value = (++i).toString()

        Log.d(TAG, "glideLoad ${text.value}")
        test()
    }

    fun test() {
//        ProxyTest.testJava()
        ProxyTest().testCallback()
    }

    fun loadByCoroutine() {
        Log.d(TAG, "loadByCoroutine")
        viewModelScope.launch {
            Log.d(TAG, "viewModelScope launch")
            text.value = "preload"
            val result = mockInternet()
            Log.d(TAG, "set $result to text")
            text.value = result
        }
    }

    private suspend fun mockInternet(): String? = withContext(Dispatchers.IO) {
        Log.d(TAG, "mock internet coroutine")
        delay(3000)
//        getUserFromServer()
        getFanyiFromServer()

    }

    private fun getUserFromServer() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.getUser("Guolei1130").enqueue(object : Callback<TestUser> {
            override fun onResponse(call: Call<TestUser>, response: Response<TestUser>) {
                Log.d(TAG, "onResponse ${response.body()}")
                text.postValue(response.body()?.bio ?: "")
            }

            override fun onFailure(call: Call<TestUser>, t: Throwable) {
                text.postValue("onFailure")
            }
        })
    }

    private fun getFanyiFromServer(): String? {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://fanyi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val retrofitService = retrofit.create(FanyiRetrofitService::class.java)
        val proxy = Proxy.newProxyInstance(FanyiRetrofitService::class.java.classLoader, arrayOf(FanyiRetrofitService::class.java),
                object : InvocationHandler {
                    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
                        Log.d(TAG, "invocation before invoke $this; $method; $args")
                        if (method.declaringClass == Any::class.java) {
                            return method.invoke(this, null)
                        }
                        return null
                    }
                }) as FanyiRetrofitService?
//        val javaProxy = ProxyTest().proxyTest(FanyiRetrofitService::class.java) // 可行，不崩
//        val ktProxy = ProxyTestKt().proxyTest(FanyiRetrofitService::class.java) // 照样崩，看来与kotlin有关
//        Log.d(TAG, "invoke result ${ktProxy.hashCode()}")
        // 异步
        /*retrofitService.getFanyi("Hello, killer").enqueue(object :Callback<FanyiResponse> {
            override fun onResponse(call: Call<FanyiResponse>, response: Response<FanyiResponse>) {
                text.postValue(response.body()?.translateResult?.get(0)?.get(0)?.tgt)
            }

            override fun onFailure(call: Call<FanyiResponse>, t: Throwable) {
                text.postValue("onFailure $call $t")
            }
        })*/
        // rxjava配合retrofit
        retrofitService.test("京多安")
                .subscribeOn(Schedulers.io()) //事件执行在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(object: rx.Observer<FanyiResponse> {
                    override fun onCompleted() {
                    }
                    override fun onError(e: Throwable?) {
                    }
                    override fun onNext(t: FanyiResponse?) {
                    }
                })
        Log.d(TAG, "getFanyi before execute")
        // 同步
        val result = retrofitService.getFanyi("你好，西八", "ZH_CN2JA").execute().body()?.translateResult?.get(0)?.get(0)?.tgt
        return result
    }

    fun testOkHttp() {
        val okHttpClient = OkHttpClient.Builder()
                .dispatcher(Dispatcher())
                .build()
        val requestBody = FormBody.Builder()
                .add("name", "post")
                .add("pwd", "password")
                .build()
        val request = Request.Builder()
                .url(URL("https://www.baidu.com/"))
                .post(requestBody)
                .build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            }
        })
        dss = "wuwu"
        Log.d(TAG, "testOkHttp: $dss")

    }

    var dss: String = ""
        get() {
            return field + "hehe"
        }
        set(value) {
            field = dss + value
        }

    override fun onCleared() {
        super.onCleared()
        dss = "wuwu"
        directlyLoadJob.cancel()
        Log.d(TAG, "onCleared $dss")
    }
}