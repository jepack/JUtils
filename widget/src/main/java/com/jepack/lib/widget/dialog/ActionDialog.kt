package com.jepack.lib.widget.dialog

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.view.View
import com.jepack.lib.widget.R
import com.jepack.lib.widget.BR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import com.jepack.lib.widget.databinding.ActionDialogBinding

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/6/20.
 */
open class ActionDialog: BindingDialog<ActionDialogBinding>() {
    private lateinit var actionModel:ActionModel
    private var cacheModel:ActionModel? = null
    var onKeyListener: BindingDialog.OnKeyListener? = null
    var canCustom:Boolean = false
    companion object {
        const val TAG = "ACTION_DIALOG"
    }
    private val publisher: PublishSubject<Action> = PublishSubject.create()
    override fun layoutId(): Int {
        return R.layout.dialog_action
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionModel = getLiveData()
        binding?.setVariable(BR.action, actionModel)
        actionModel.cloneValue(cacheModel)
        binding?.dialogOkBtn?.setOnClickListener(View.OnClickListener {
            publisher.onNext(Action(Action.ACTION_BASE + 1, actionModel.msg.value))

        })
    }

    public fun show(model:ActionModel, fm: FragmentManager, tag:String){

        if(isAdded && isVisible){
            this.cacheModel = model
            actionModel.cloneValue(actionModel)
        }else {
            this.cacheModel = model
            val transaction = fm.beginTransaction().addToBackStack(tag)
            show(transaction, tag)
        }
    }

    public fun registerActionObserver(observer: Consumer<Action>){
        publisher.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .distinct {
                    it.data.toString().hashCode() //防止URl重复上报
                }
                .subscribe(observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        publisher.unsubscribeOn(AndroidSchedulers.mainThread())
    }

    override fun onStart() {
        super.onStart()
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
}