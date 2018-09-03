package com.jepack.jutils.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.internal.ShowFirstParty
import com.jepack.skeleton.SkeletonUtil
import com.wmkankan.audio.R
import com.wmkankan.audio.settings.SettingsFragment
import com.wmkankan.audio.widget.dialog.*
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Fragment 基础类，提供基本功能
 * Created by zhanghaihai on 2018/5/29.
 */
abstract class BaseFragment : Fragment(), ActFgInteractive {
    open var binding:ViewDataBinding? = null
    private var layoutId:Int? = null
    var loadingDialog: LoadingDialog? = null
    var showDialogRunnable:Runnable? = null
    var showActRunnable:Runnable? = null
    var showMaskRunnable:Runnable? = null
    var isPaused = true //默认值为True，防止尚未onResume前onSaveInstanceState
    companion object {
        const val MASK_TAG:String = "MASK_FG"
        const val ACT_TAG:String = "ACT_FG"
        const val LOAD_TAG:String = "LOAD_DIALOG"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.executePendingBindings()
    }

    override fun onBackPressed():Boolean {
        val maskRemoved = removeMaskFg()
        return if(maskRemoved){
            true
        }else {
            activity?.finish()
            false
        }
    }

    override fun onNewActIntent(intent: Intent) {

    }

    @CallSuper
    open fun inflateContentLayout(layoutId:Int, inflater: LayoutInflater, container: ViewGroup?):View?{
        this.layoutId = layoutId;
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.setLifecycleOwner(this)
        return binding?.root
    }

    //内联
    inline fun <reified T : ViewModel?> getLiveData():T{
        return ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory()).get(T::class.java)
    }

    //内联
    inline fun <reified T : ViewModel?> getLiveData(id:String):T{
        return ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory()).get(id, T::class.java)
    }

    /**
     * 显示加载弹框
     */
    fun showLoading(msg:String){
        synchronized(this) {
            showDialogRunnable = Runnable {
                if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    showDialogRunnable = null
                    loadingDialog?.show(msg, childFragmentManager, LOAD_TAG) ?: LoadingDialog().let {
                        loadingDialog = it
                        it.onKeyListener = object: BindingDialog.OnKeyListener{
                            override fun onBackPress():Boolean {
                                return this@BaseFragment.onBackPressed()
                            }

                        }
                        it.show(msg, childFragmentManager, LOAD_TAG)
                    }

                }

            }

            if (!isPaused) {
                showDialogRunnable?.run()
                showDialogRunnable = null
            }
        }

    }

    //隐藏加载弹框
    fun hideLoading(){
        loadingDialog?.safeDismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        synchronized(this) {
            showDialogRunnable?.run()
            showDialogRunnable = null
            showMaskRunnable?.run()
            showMaskRunnable == null
        }
    }

    fun showMaskFg(fragment: BaseFragment?){
        synchronized(this) {
            showMaskRunnable = Runnable {
                removeMaskFg()
                fragment?.let {
                    activity?.supportFragmentManager?.beginTransaction()
                            ?.addToBackStack(MASK_TAG)
                            ?.add(R.id.base_mask_fg_container, it, MASK_TAG)
                            ?.commit()
                }
            }

            if (!isPaused) {
                showMaskRunnable?.run()
                showMaskRunnable = null
            }
        }

    }

    fun removeMaskFg():Boolean{
        //清空蒙版Fragment
        synchronized(this) {
            showMaskRunnable = null
        }
        return if(activity?.supportFragmentManager?.findFragmentByTag(MASK_TAG) != null) {
            activity?.supportFragmentManager?.popBackStack(MASK_TAG, POP_BACK_STACK_INCLUSIVE)
            true
        }else{
            false
        }
    }

    fun showAction(actionModel: ActionModel, observer: Consumer<Action>){
        synchronized(this) {
            showActRunnable = Runnable {
                fragmentManager?.let {
                    dismissActionDialog()
                    val dialog = ActionDialog().let {
                        it.onKeyListener = object : BindingDialog.OnKeyListener {
                            override fun onBackPress(): Boolean {
                                return this@BaseFragment.onBackPressed()
                            }

                        }
                        it
                    }
                    dialog.registerActionObserver(observer)
                    dialog.show(actionModel, it, ACT_TAG)
                }
            }

            if (!isPaused) {
                showActRunnable?.run()
                showActRunnable = null
            }
        }
    }

    fun dismissActionDialog(){
        fragmentManager?.popBackStack(ACT_TAG, POP_BACK_STACK_INCLUSIVE)
    }
}
