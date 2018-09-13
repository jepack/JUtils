package com.jepack.lib.widget

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller


class SlidingLayout: FrameLayout {
    // 页面边缘阴影的宽度默认值
    private val SHADOW_WIDTH = 16
    private var mActivity: Activity? = null
    private var mScroller: Scroller? = null
    // 页面边缘的阴影图
    private var mLeftShadow: Drawable? = null
    // 页面边缘阴影的宽度
    private var mShadowWidth: Int = 0
    private var mInterceptDownX: Int = 0
    private var mLastInterceptX: Int = 0
    private var mLastInterceptY: Int = 0
    private var mTouchDownX: Int = 0
    private var mLastTouchX: Int = 0
    private var mLastTouchY: Int = 0
    private var isConsumed = false

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        mScroller = Scroller(context)
        mLeftShadow = ContextCompat.getDrawable(context, R.drawable.left_shadow)
        val density = resources.displayMetrics.density.toInt()
        mShadowWidth = SHADOW_WIDTH * density
    }

    /**
     * 绑定Activity
     */
    fun bindActivity(activity: Activity) {
        mActivity = activity
        val decorView = mActivity!!.window.decorView as ViewGroup
        val child = decorView.getChildAt(0)
        decorView.removeView(child)
        addView(child)
        decorView.addView(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
                mInterceptDownX = x
                mLastInterceptX = x
                mLastInterceptY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastInterceptX
                val deltaY = y - mLastInterceptY
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，拦截事件
                if (mInterceptDownX < getWidth() / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true
                } else {
                    intercept = false
                }
                mLastInterceptX = x
                mLastInterceptY = y
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                mLastInterceptY = 0
                mLastInterceptX = mLastInterceptY
                mInterceptDownX = mLastInterceptX
            }
        }
        return intercept
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchDownX = x
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastTouchX
                val deltaY = y - mLastTouchY

                if (!isConsumed && mTouchDownX < getWidth() / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                    isConsumed = true
                }

                if (isConsumed) {
                    val rightMovedX = mLastTouchX - ev.x.toInt()
                    // 左侧即将滑出屏幕
                    if (getScrollX() + rightMovedX >= 0) {
                        scrollTo(0, 0)
                    } else {
                        scrollBy(rightMovedX, 0)
                    }
                }
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_UP -> {
                isConsumed = false
                mLastTouchY = 0
                mLastTouchX = mLastTouchY
                mTouchDownX = mLastTouchX
                // 根据手指释放时的位置决定回弹还是关闭
                if (-getScrollX() < getWidth() / 2) {
                    scrollBack()
                } else {
                    scrollClose()
                }
            }
        }
        return true
    }

    /**
     * 滑动返回
     */
    private fun scrollBack() {
        val startX = getScrollX()
        val dx = -getScrollX()
        mScroller!!.startScroll(startX, 0, dx, 0, 300)
        invalidate()
    }

    /**
     * 滑动关闭
     */
    private fun scrollClose() {
        val startX = getScrollX()
        val dx = -getScrollX() - getWidth()
        mScroller!!.startScroll(startX, 0, dx, 0, 300)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, 0)
            postInvalidate()
        } else if (-getScrollX() >= getWidth()) {
            mActivity!!.finish()
        }
    }

    protected override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawShadow(canvas)
    }

    /**
     * 绘制边缘的阴影
     */
    private fun drawShadow(canvas: Canvas) {
        mLeftShadow!!.setBounds(0, 0, mShadowWidth, getHeight())
        canvas.save()
        canvas.translate((-mShadowWidth).toFloat(), 0F)
        mLeftShadow!!.draw(canvas)
        canvas.restore()
    }
}