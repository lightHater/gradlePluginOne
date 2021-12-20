package com.example.textlinkdemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

class MyLinearLayout @JvmOverloads constructor(private val viewContext: Context,
                                               attrs: AttributeSet? = null, attr: Int = 0):
        LinearLayout(viewContext, attrs, attr) {

    /*
    * public MyLinearLayout(@NotNull Context viewContext, @Nullable AttributeSet attrs, int attr) {
      Intrinsics.checkParameterIsNotNull(viewContext, "viewContext");
      super(viewContext, attrs, attr);
      this.viewContext = viewContext;
      this.initView();
   }
    * */

    companion object {
        private const val TAG = "MyLinearLayout"
    }

    init {
        initView()
    }

    private fun initView() {
        Log.d(TAG, "initView: ")
        val looper = viewContext.mainLooper
    }
}