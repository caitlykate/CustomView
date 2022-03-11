package com.caitlykate.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class CometTouchView @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.CometTouchViewStyle,
    defStyleRes: Int = R.style.DefaultCometViewStyle,
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    companion object {
        private const val COMET_DEFAULT_COLOR = R.color.light_gray
    }

    private val cometColor: Int

    // координаты для рисования круга
    private var x: Float? = 0f
    private var y: Float? = 0f

    // движение пальца
    private var isActionMove: Boolean = false

    private val circlePaint: Paint by lazy(LazyThreadSafetyMode.NONE) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = getCompatColor(colorRes = COMET_DEFAULT_COLOR)
            maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)

        }
    }

    init {
        if (attributesSet != null) {
            initAttributes(attributesSet, defStyleAttr, defStyleRes)
            val typedArray =
            cometColor =
        } else {
            cometColor = getCompatColor(colorRes = COMET_DEFAULT_COLOR)
        }
    }

    private fun getTypedArray(
        context: Context,
        attributesSet: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int,
    ): TypedArray {
        return context.obtainStyledAttributes(
            attributesSet,
            R.styleable.CometTouchView,
            defStyleAttr,
            defStyleRes
        )
    }

    private fun getDefaultCometColor(
        context: Context,
        typedArray: TypedArray,
    ): Int {
        return typedArray.getColor(
            R.styleable.CometTouchView_cometColor,
            getCompatColor(colorRes = COMET_DEFAULT_COLOR)
        )
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributesSet,
            R.styleable.CometTouchView,
            defStyleAttr,
            defStyleRes
        )

        cometColor = typedArray.getColor(
            R.styleable.CometTouchView_cometColor,
            getCompatColor(colorRes = COMET_DEFAULT_COLOR)
        )

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isActionMove) {
            canvas.drawCircle(x!!, y!!, 50f, circlePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_DOWN -> {
                isActionMove = true
                x = event.x
                y = event.y
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isActionMove = false
                invalidate()
                return true
            }
        }
        return true
    }
}

fun Context.getCompatColor(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun View.getCompatColor(@ColorRes colorRes: Int): Int {
    return context.getCompatColor(colorRes = colorRes)
}