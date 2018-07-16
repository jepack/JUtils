package com.jepack.jutils.base

import android.app.Application
import android.content.Context
import com.jepack.lib.utils.AppUtils
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/6/4.
 */
class JUtilApp : Application() {

    companion object {
        lateinit var appContext:Context

    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        //添加友盟相关信息
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        val channel = AppUtils.getMetaData(this, "UMENG_CHANNEL")
        UMConfigure.init(this, "5b4b6693f43e485d0700046a", channel, UMConfigure.DEVICE_TYPE_PHONE, "")

    }

}