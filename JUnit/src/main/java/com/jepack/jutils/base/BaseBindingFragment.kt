package com.jepack.jutils.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jepack.jutils.R

/**
 * TODO Add class comments here.
 * Created by zhanghaihai on 2018/5/29.
 */
abstract class BaseBindingFragment<T: ViewDataBinding> : BaseFragment(), ActFgInteractive {
    open var actBinding:T? = null
    var toolbar:Toolbar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inflateContentLayout(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = super.inflateContentLayout(layoutId, inflater, container)
        actBinding = binding as T
        return view
    }

    fun initToolbar(): Toolbar? {
        actBinding?.root?.findViewById<View>(R.id.toolbar)?.let {
            toolbar = it.findViewById<View>(R.id.toolbar) as Toolbar
        }
        (activity != null && activity is AppCompatActivity).let {
            if(it) {
                val appCompatActivity = activity as AppCompatActivity
                appCompatActivity.setSupportActionBar(toolbar)
                if (appCompatActivity.supportActionBar != null) {
                    // Enable the Up button
                    appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    appCompatActivity.supportActionBar!!.setDisplayShowTitleEnabled(false)
                }
            }
        }
        return toolbar
    }
}
