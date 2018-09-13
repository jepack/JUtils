package com.jepack.jutils.base

import android.content.Intent

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/5/30.
 */
interface ActFgInteractive {
    fun onBackPressed():Boolean
    fun onNewActIntent(intent: Intent)
}