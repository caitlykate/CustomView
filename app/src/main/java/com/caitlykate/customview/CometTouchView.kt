package com.caitlykate.customview

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.properties.Delegates

class CometTouchView(
    context: Context,
    attributesSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) : View(context, attributesSet, defStyleAttr, defStyleRes), View.OnTouchListener {

    private var cometColor by Delegates.notNull<Int>()

    val COMET_DEFAULT_COLOR = Color.argb(127, 255, 0, 0)

    // координаты для рисования круга
    var x : Float? = 0f
    var y : Float? = 0f

    // движение пальца
    var isActionMove = false

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = COMET_DEFAULT_COLOR
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)

    }
    private val shadowPaint = Paint(0).apply {
        style = Paint.Style.FILL
        color = 0x101010
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    constructor(context: Context, attributesSet: AttributeSet?, defStyleAttr: Int
    ) : this(context, attributesSet, defStyleAttr,  R.style.DefaultCometViewStyle)
    constructor(context: Context, attributesSet: AttributeSet?) : this(context, attributesSet, R.attr.CometTouchViewStyle)
    constructor(context: Context) : this(context, null)

    init {
        if (attributesSet != null) {
            initAttributes(attributesSet, defStyleAttr, defStyleRes)
        } else {
            initDefaultColor()
        }
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributesSet, R.styleable.CometTouchView, defStyleAttr, defStyleRes)

        cometColor = typedArray.getColor(R.styleable.CometTouchView_cometColor, COMET_DEFAULT_COLOR)

        typedArray.recycle()
    }

    private fun initDefaultColor(){
        cometColor = COMET_DEFAULT_COLOR
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(x!!, y!!, 50f, circlePaint)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // координаты Touch-события
        var xEv = event?.x
        var yEv = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {// нажатие
                // включаем режим перетаскивания
                isActionMove = true
            }
            MotionEvent.ACTION_MOVE -> {// движение
                // если режим перетаскивания включен
                if (isActionMove) {
                    // определеяем новые координаты для рисования
                    x = xEv!!
                    y = yEv!!
                    // перерисовываем экран
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {// отпускание
                // выключаем режим перетаскивания
                isActionMove = false
            }
            MotionEvent.ACTION_CANCEL -> {
                // выключаем режим перетаскивания
                isActionMove = false
            }
        }
        return true;
    }

    companion object{
        //const val COMET_DEFAULT_COLOR = Color.
    }
}