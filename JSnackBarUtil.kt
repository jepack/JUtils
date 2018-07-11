package com.btkanba.player.play.widget

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.btkanba.player.common.UtilBase
import com.btkanba.player.play.R

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/7/10.
 */
class JSnackBarUtil {
    companion object {
        const val Info = 1
        const val Confirm = 2
        const val Warning = 3
        const val Alert = 4
    }

    private var jSnackBar:JSnackBar? = null
    fun showLongSnackBar(container: View, msg: String, level: Int) {
        jSnackBar?.let {
            it.setMsg(msg)?.let{
                setSnackBarLevel(it, level)
                it.show()
            }

        }?: (JSnackBar()).let{
            jSnackBar = it
            it.make(container,  msg, 3000, R.layout.snack_bar_full_width).let{
                setSnackBarLevel(it, level)
                it.show()
            }
        }



    }

    private fun setSnackBarLevel(snackBar: Snackbar, level:Int){
        snackBar.view.findViewById<TextView>(R.id.jepack_snack_bar_msg)?.let{
            val bgColor = when(level){
                Info ->{
                    R.color.snack_bar_info
                }
                Confirm->{
                    R.color.snack_bar_confirm
                }
                Warning ->{
                    R.color.snack_bar_warning
                }
                Alert ->{
                    R.color.snack_bar_alert
                }else ->{
                    R.color.snack_bar_info
                }
            }
            it.setBackgroundColor(ContextCompat.getColor(UtilBase.getAppContext(), bgColor))
        }

    }
}
