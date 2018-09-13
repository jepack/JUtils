package com.jepack.lib.widget.dialog

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/6/16.
 */
data class ActionModel(var msg:MutableLiveData<String>, var tip:MutableLiveData<String>, var btnMsg:MutableLiveData<String>): ViewModel() {
    constructor(_msg:String, _tip:String, _btnMsg:String) : this(MutableLiveData<String>(), MutableLiveData<String>(), MutableLiveData<String>()) {
        this.msg.value = _msg
        this.tip.value = _tip
        this.btnMsg.value = _btnMsg
    }

    constructor():this("", "", "")


    fun cloneValue(actionModel:ActionModel?){
        this.msg.value = actionModel?.msg?.value
        this.tip.value = actionModel?.tip?.value
        this.btnMsg.value = actionModel?.btnMsg?.value
    }

    override fun toString(): String {
        return this.msg.value?:""
    }
}