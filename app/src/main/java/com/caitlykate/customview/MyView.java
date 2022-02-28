package com.caitlykate.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

class MyView extends View {
    Paint p;
    // координаты для рисования квадрата
    float x = 100;
    float y = 100;
    int side = 100;

    // переменные для перетаскивания
    boolean drag = false;
    float dragX = 0;
    float dragY = 0;

    public MyView(Context context) {
        super(context);
        p = new Paint();
        p.setColor(Color.GREEN);
    }

    protected void onDraw(Canvas canvas) {
        // рисуем квадрат
        canvas.drawRect(x, y, x + side, y + side, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:
                // если касание было начато в пределах квадрата
                if (evX >= x && evX <= x + side && evY >= y && evY <= y + side) {
                    // включаем режим перетаскивания
                    drag = true;
                    // разница между левым верхним углом квадрата и точкой касания
                    dragX = evX - x;
                    dragY = evY - y;
                }
                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                // если режим перетаскивания включен
                if (drag) {
                    // определеяем новые координаты для рисования
                    x = evX - dragX;
                    y = evY - dragY;
                    // перерисовываем экран
                    invalidate();
                }
                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
                // выключаем режим перетаскивания
                drag = false;
                break;
        }
        return true;
    }
}