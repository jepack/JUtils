package com.jepack.lib.widget.dialog

import android.app.Dialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.jepack.lib.widget.R

/**
 * 具备单个点击按钮的提示框
 * Created by zhanghaihai on 2018/6/15.
 */
abstract class BindingDialog<T: ViewDataBinding>: DialogFragment() {
    var binding:T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflateContentLayout(layoutId(), inflater, container)
    }

    public abstract fun layoutId():Int

    open fun inflateContentLayout(layoutId:Int, inflater: LayoutInflater, container: ViewGroup?):View?{
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.setLifecycleOwner(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //无title样式
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //无边框
        setStyle(DialogFragment.STYLE_NO_FRAME, 0)

        //设置背景透明
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setWindowAnimations(R.style.DF_NO_PADDING_TRANSPARENT)
    }

    /**
     * 防止出现Activity被销毁时dismiss导致的异常
     * Activity 被销毁时无需再销毁了
     */
    public fun safeDismiss(){
        if(lifecycle.currentState != Lifecycle.State.DESTROYED){
            dismiss()
        }else{
            //Nothing is need to do.
        }
    }

    //内联
    inline fun <reified T : ViewModel?> getLiveData():T{
        return ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory()).get(T::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, R.style.DF_NO_PADDING_TRANSPARENT) {

            override fun onBackPressed() {
                dismiss()
            }

            override fun onTouchEvent(event: MotionEvent): Boolean {

                return false
            }

        }
    }

    interface OnKeyListener{
        fun onBackPress():Boolean
    }

}