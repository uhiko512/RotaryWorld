package tw.rmstudio.uhiko.rotaryworld.game.view.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import tw.rmstudio.uhiko.rotaryworld.SoundManeger;

/**
 * Created by uhiko on 14/12/30.
 */
public abstract class Controller extends View implements View.OnTouchListener {
    protected SoundManeger soundManeger;

    protected float cx, cy;
    protected float radius;
    protected Paint paint;
    protected int defaultColor;
    protected int touchColor;

    public Controller(Context context) {
        super(context);
        soundManeger = SoundManeger.getInstance(getContext());
        paramsInit();
        paint.setColor(defaultColor);
        paint.setAlpha(150);
        setLayout();
        setOnTouchListener(this);
    }

    protected void setLayout() {
        setX(cx - radius);
        setY(cy - radius);
        setLayoutParams(new ViewGroup.LayoutParams((int) radius * 2, (int) radius * 2));
    }

    public abstract void paramsInit();
    public abstract void actionDown();
    public abstract void actionUp();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(radius, radius, radius, paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                actionUp();
                break;
        }
        invalidate();

        return true;
    }
}
