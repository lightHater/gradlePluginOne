package com.example.textlinkdemo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.textlinkdemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class Main2Activity : AppCompatActivity() {

    companion object {
        const val TAG = "Main2Activity"
    }

    private val mViewModel: Main2ViewModel by lazy {
        ViewModelProvider(this)
                .get(Main2ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mViewModel = mViewModel
        // 坑：手动设置后，xml中所有livedata的变化可自动更新到ui；若不设置，需手动observe每个livedata然后更新ui
        binding.lifecycleOwner = this

        // 手动监听livedata更新ui
        /*viewModel.text.observe(this, object: Observer<String>{
            override fun onChanged(p0: String?) {
                text.text = p0
            }

        })*/
//        lifecycle.addObserver(MyLifeCycleObserver())

        image.setOnClickListener {
            image.isActivated = !image.isActivated
//            startImageAnimator(image)

        }
        val addArgs: (Intent.(Int) -> Unit) = {
            putExtra("from", it + 3)
        }
        text.setOnClickListener {
            startMyActivity<MainActivity> (2) {
                5 + 2
            }
        }

        mViewModel.lifeLiveDataTest.observe(this) {
            Log.d(TAG, "lifeLiveDataTest onChanged: $it")
        }
        mViewModel.lifeLiveDataTest.value = "onCreate"

    }

    override fun onResume() {
        super.onResume()
        mViewModel.lifeLiveDataTest.value = "onResume"
    }

    override fun onPause() {
        super.onPause()
        //做这个实验是为了验证livedata的回调时机，只有lifecycleowner的状态为asLeast Started(Started, Resumed)才会
        //将数据的变化通知到observer，即onStart被调用后到onPause被调用前这段时间，onPause这里就通知不过去了
        //源码见LiveData.considerNotify
        mViewModel.lifeLiveDataTest.value = "onPause"
    }

    override fun onStart() {
        super.onStart()
        mViewModel.lifeLiveDataTest.value = "onStart"
    }

    override fun onStop() {
        super.onStop()
        mViewModel.lifeLiveDataTest.value = "onStop"
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.lifeLiveDataTest.value = "onDestroy"
    }

    fun startImageAnimator(image: ImageView) {
        val yAnimator: Animator = if (image.isActivated) {
            ObjectAnimator.ofFloat(image, "scaleY", image.scaleY, 2f)
        } else {
            ObjectAnimator.ofFloat(image, "scaleY", image.scaleY, 1f)
        }
        val expand =  image.isActivated
        yAnimator.duration = 2000
//        yAnimator.setAutoCancel(true)
        yAnimator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
                Log.d(TAG, "onAnimationStart $expand")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d(TAG, "onAnimationEnd $expand")
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.d(TAG, "onAnimationCancel $expand")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                Log.d(TAG, "onAnimationRepeat")
            }

        })
        yAnimator.start()
    }


}