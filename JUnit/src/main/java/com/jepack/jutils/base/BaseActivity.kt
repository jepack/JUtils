package com.wmkankan.audio.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent

import com.wmkankan.audio.R
import com.wmkankan.audio.widget.SlidingLayout

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/5/29.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var baseFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayoutId())
        baseFragment = instanceHolderFragment(this)
        if (baseFragment != null) {
            supportFragmentManager.beginTransaction().add(R.id.base_fg_container, baseFragment).commit()
        }
    }

    abstract fun instanceHolderFragment(activity: BaseActivity): BaseFragment

    override fun onBackPressed() {
        if (baseFragment != null) {
            if(!baseFragment!!.onBackPressed()){
                super.onBackPressed()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (baseFragment != null) {
            baseFragment!!.onNewActIntent(intent)
        }
    }

    open fun initLayoutId():Int{
        return R.layout.act_base
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    fun enableSlide(){
        val rootView = SlidingLayout(this)
        rootView.bindActivity(this)
    }

}
