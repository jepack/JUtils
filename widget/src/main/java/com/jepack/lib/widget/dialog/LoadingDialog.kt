package com.jepack.lib.widget.dialog

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.jepack.lib.widget.R
import com.jepack.lib.widget.BR
import com.jepack.lib.widget.databinding.LoadDialogBinding
import javax.inject.Inject

/**
 * 加载弹窗
 * Created by zhanghaihai on 2018/6/15.
 */
class LoadingDialog @Inject constructor(): BindingDialog<LoadDialogBinding>() {
    private lateinit var loadingModel: LoadingModel
    var msg:String = ""
    var onKeyListener:OnKeyListener? = null
    var canCustom:Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingModel = getLiveData()
        binding?.setVariable(BR.loading, loadingModel)
        loadingModel.msg.value = msg
    }

    override fun layoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun onStart() {
        super.onStart()
        loadAnimation()
        dialog.setOnKeyListener { dialog, keyCode, event ->
            //返回键处理，设置了返回监听事件，处理返回事件。没有设置时不阻塞返回键。
            if(keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP){
                if(canCustom) onKeyListener?.onBackPress()?:false
                else false

            }else if(keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                canCustom = true
                false
            }else {
                false
            }

        }
    }

    override fun onStop() {
        super.onStop()
        clearAnimation()
    }

    private fun loadAnimation(){
        binding?.dialogLoadingProgress?.animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate)
        binding?.dialogLoadingProgress?.animation?.start()
    }

    private fun clearAnimation(){
        binding?.dialogLoadingProgress?.clearAnimation()
        binding?.dialogLoadingProgress?.animate()?.cancel()
    }

    public fun show(msg:String, fm:FragmentManager, tag:String){
        this.msg = msg
        if(isVisible){
            loadingModel.msg.value = msg
        }else {
            val transaction = fm.beginTransaction().addToBackStack(tag)
            show(transaction, tag)
        }
    }

}