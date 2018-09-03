package com.jepack.lib.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View

import com.jepack.lib.utils.DisplayUtil

/**
 * Add by zhh 2017/5/8
 */
class ColorFillProgressView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var progress = 50

    private var leftPadding: Int = 0
    private var topPadding: Int = 0

    private var rightPadding: Int = 0
    private var bottomPadding: Int = 0


    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var bg: Bitmap? = null
    private var mask: Bitmap? = null
    private var paint: Paint? = null

    private var max = 1000;


    companion object {
        const val DIRECTION_LTR = 0 //
        const val DIRECTION_RTL = 1
        const val DIRECTION_BTT = 2
        const val DIRECTION_TTB = 3

        @IntDef(value = [DIRECTION_LTR, DIRECTION_RTL, DIRECTION_BTT, DIRECTION_TTB])
        @Retention(AnnotationRetention.SOURCE)
        annotation class DIRECTION
    }

    @DIRECTION
    private var direction = DIRECTION_BTT

    constructor(context: Context) : this(context, null) {
        init()
    }

    init {
        init()
    }

    private fun init() {

        bg = BitmapFactory.decodeResource(resources, R.drawable.ic_tree)
        mask = BitmapFactory.decodeResource(resources, R.drawable.ic_tree_colorfull)
        leftPadding = DisplayUtil.dp2px(context, 0f)
        rightPadding = DisplayUtil.dp2px(context, 4f)
        mWidth = bg!!.width - leftPadding - rightPadding

        topPadding = DisplayUtil.dp2px(context, 0f)
        bottomPadding = DisplayUtil.dp2px(context, 0f)
        mHeight = bg!!.width - topPadding - bottomPadding

        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {

        // draw the background
        canvas.drawBitmap(bg!!, 0f, 0f, null)
        // 绘制显示电量部分
        canvas.save()
        bg?.extractAlpha()
        when(direction){
            DIRECTION_LTR -> {
                canvas.clipRect(0, 0, width, calculateClipBottom(progress))
            }
            DIRECTION_RTL -> {
                canvas.clipRect(0, 0, calculateClipRight(progress), height)
            }
            DIRECTION_TTB ->{
                canvas.clipRect(calculateClipLeft(progress), 0, width, height)
            }
            else ->{
                canvas.clipRect(0, calculateClipTop(progress), width, height)
            }
        }

        canvas.drawBitmap(mask!!, 0f, 0f, null)
        canvas.restore()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var width = widthSize
        var height = heightSize
        if (View.MeasureSpec.AT_MOST == widthMode) {
            width = bg!!.width
        }
        if (View.MeasureSpec.AT_MOST == heightMode) {
            height = bg!!.height
        }
        setMeasuredDimension(width, height)

    }

    /**
     *
     * @param progress
     * between 0 and max
     */
    fun setProgress(progress: Int) {
        if (progress < 0 || progress > max) {
            return
        }
        this.progress = progress
        postInvalidate()
    }

    private fun calculateClipRight(progress: Int): Int {
        return mWidth * progress / max + leftPadding
    }

    private fun calculateClipBottom(progress: Int): Int {
        return mHeight * progress / max + topPadding
    }

    private fun calculateClipLeft(progress: Int): Int {
        return mWidth - (mWidth * progress / max + leftPadding)
    }

    private fun calculateClipTop(progress: Int): Int {
        return mHeight - (mHeight * progress / max + topPadding)
    }

    public fun setMax(max:Int){
        this.max = max
    }

    public fun getMax():Int{
        return max
    }
}
