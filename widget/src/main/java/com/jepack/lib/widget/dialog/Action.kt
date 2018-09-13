package com.jepack.lib.widget.dialog

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/6/20.
 */
data class Action(var action:Int, var data:Any? = null, var arg:Int = -1, var arg2:String = "", var obj:Any? = null) {
    companion object {
        const val ACTION_BASE:Int = 0
    }
}