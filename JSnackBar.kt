package com.btkanba.player.play.widget

import android.graphics.Color
import android.support.design.internal.SnackbarContentLayout
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.btkanba.player.common.LogUtil
import com.btkanba.player.play.R

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/7/1.
 */
class JSnackBar{
    private lateinit var snackbar: Snackbar
    private lateinit var msgView:TextView
    private lateinit var actBtn:Button
    /**
     *
     */
    fun make(view: View, msg:String, duration: Int, layout:Int):Snackbar{
        snackbar = Snackbar.make(view, msg, duration)
        val parent = snackbar.view as Snackbar.SnackbarLayout
        parent.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        parent.setBackgroundColor(Color.TRANSPARENT)
        parent.setPadding(0, 0,0, 0)
        val contentView = parent.getChildAt(0) as SnackbarContentLayout
        val newContentView = LayoutInflater.from(view.context).inflate(layout, contentView, false) as SnackbarContentLayout
        //重新制定ID
        newContentView.id = contentView.id
        msgView = newContentView.findViewById(R.id.jepack_snack_bar_msg)
        actBtn  = newContentView.findViewById(R.id.jepack_snack_bar_action)
        //重置Id
        msgView.id = android.support.design.R.id.snackbar_text
        actBtn.id = android.support.design.R.id.snackbar_action
        //重新获取messageView
        //重新获取actionView
        snackbar.view.setPadding(0, 0,0,0)
        try {

            val method = newContentView::class.java.getDeclaredMethod("onFinishInflate")
            method.isAccessible = true
            method.invoke(newContentView)
            //设置消息文本
            msgView.text = msg
            val index = parent.indexOfChild(contentView)
            //替换布局
            parent.removeViewAt(index)
            parent.addView(newContentView, index)
        }catch (e:Exception){
            LogUtil.e("Failed to change snackbar layout! ", e.message)
        }
        return snackbar
    }

    public fun setMsg(msg:String):Snackbar?{
        snackbar.setText(msg)
        return snackbar
    }


}