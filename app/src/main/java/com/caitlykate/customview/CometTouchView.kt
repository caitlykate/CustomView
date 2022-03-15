package com.caitlykate.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.*

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

    //координаты следа пальца
    private val pointsLinkedList = LinkedList<Point>()

    init {
        val typedArray = attributesSet?.let { attrSet ->
            context.obtainStyledAttributes(
                attrSet,
                R.styleable.CometTouchView,
                defStyleAttr,
                defStyleRes
            )
        }

        cometColor = typedArray?.getColor(
            R.styleable.CometTouchView_cometColor,
            getCompatColor(colorRes = COMET_DEFAULT_COLOR)
        ) ?: getCompatColor(colorRes = COMET_DEFAULT_COLOR)

        typedArray?.recycle()
    }

    private val circlePaint: Paint by lazy(LazyThreadSafetyMode.NONE) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = cometColor
            maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)

        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pointsLinkedList.forEachIndexed { index, point ->
            canvas.drawCircle(
                point.x.toFloat(),
                point.y.toFloat(),
                (index + 1).toFloat(),
                circlePaint,
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_DOWN -> {
                pointsLinkedList.add(Point(event.x.toInt(), event.y.toInt()))

                if (pointsLinkedList.size > 50) {
                    pointsLinkedList.removeFirst()
                }

                invalidate()
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                pointsLinkedList.clear()
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