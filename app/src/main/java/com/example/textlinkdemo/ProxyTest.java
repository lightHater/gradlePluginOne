package com.example.textlinkdemo;

import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleService;


public class ProxyTest {

    static final String TAG = "ProxyTest";
    public <T>T proxyTest(Class<T> service) {
        return (T)Proxy.newProxyInstance(
                service.getClassLoader(),
                new Class<?>[] {service},
                new InvocationHandler() {

                    @Override
                    public @Nullable
                    Object invoke(Object proxy, Method method, @Nullable Object[] args)
                            throws Throwable {
                        Log.d(TAG, "invoke method = " + method + " args = " + args);
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            testDots();
                            testDots(args);
                            testDots(new Object[]{null});
                            return method.invoke(this, args);
                        }
                        return null;
                    }
                });
    }

    public static void testDots(Object... args) {
        Log.d(TAG, "testDots " + (args == null?null:args.length));
        new ArrayList<>();
    }

    public void testStringDots(String... args) {
    }

    static int v = 1;
    int b = v;
    int a1 = v++;
    int a2;
    static int a3 = v++;
    ProxyTest () {
        a2 = v++;
    }
    public static void testJava() {
        ProxyTest obj = new ProxyTest();
        Log.d(TAG, "testJava: " + obj.b);
        Log.d(TAG, "testJava: " + ProxyTest.v);
        Log.d(TAG, "testJava: " + obj.a1);
        Log.d(TAG, "testJava: " + obj.a2);
        Log.d(TAG, "testJava: " + ProxyTest.a3);
    }

    public void testCallback() {
        long time = System.currentTimeMillis();
        Object lock = new Object();
        Callback callback = new Callback() {
            @Override
            public void onResponse(Integer i) {
                Log.d(TAG, "onResponse: " + i);
                Log.d(TAG, "onResponse: " + (System.currentTimeMillis() - time));
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, "onFailure: " + (System.currentTimeMillis() - time));
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        };
        Log.d(TAG, "testCallback: call");
        new Api().call(callback);
        Log.d(TAG, "testCallback: wait");
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Api {
        void call(Callback callback) {
            new Thread(() -> {
                Random random = new Random(System.currentTimeMillis());
                int i = random.nextInt(100);
                Log.d(TAG, "call: " + i);
                if (i < 50) {
                    Log.d(TAG, "call: response");
                    callback.onResponse(i);
                } else {
                    Log.d(TAG, "call: failure");
                    callback.onFailure(new Exception("over 50"));
                }
            }).start();
        }
    }

    public static ProxyTest getInstance() {
        return MHolder.P;
    }

    private static class MHolder {
        private static final ProxyTest P = new ProxyTest();
    }
}

interface Callback {
    void onResponse(Integer i);
    void onFailure(Throwable e);
}
