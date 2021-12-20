package com.example.textlinkdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Utils {

    private Context mContext;

    public static void magic(TextView textView, String text) {
        textView.setAutoLinkMask(Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);
        textView.setText(text);
    }

    private Utils(Context context) {
        mContext = context;
    }

    private static Utils INSTANCE;
    public static Utils getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized(Utils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Utils(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setSmt(String s) {
        Log.d("Utils", s +" " + mContext.getPackageCodePath());
    }
}
