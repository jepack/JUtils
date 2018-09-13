package com.jepack.lib.widget.dialog

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/6/16.
 */
data class LoadingModel(var msg:MutableLiveData<String>): ViewModel() {
    constructor() : this(MutableLiveData<String>()) {
    }
}