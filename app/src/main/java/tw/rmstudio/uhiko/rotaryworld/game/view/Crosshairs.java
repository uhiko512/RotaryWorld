package tw.rmstudio.uhiko.rotaryworld.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import javax.vecmath.Vector2f;

import tw.rmstudio.uhiko.rotaryworld.game.player.OrientationSensor;

import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.height;
import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.width;

/**
 * Created by uhiko on 14/11/28.
 */
public class Crosshairs extends View implements OrientationSensor.OnOrientationChangedListener, Runnable {
    private float staticCX, staticCY;
    private float staticRadius;
    private float staticWidth;
    private Paint staticPaint;

    private float dynamicCX, dynamicCY;
    private float dynamicRadius;
    private float dynamicWidth;
    private Paint dynamicPaint;

    private Vector2f degree;

    private int color;
    private long interval;

    public Crosshairs(Context context) {
        super(context);

        staticPaint = new Paint();
        dynamicPaint = new Paint();

        color = 0xff00abff;

        staticCX = height / 2;
        staticCY = width / 2;
        staticRadius = (float) (height * 0.03);
        staticWidth = (float) (height * 0.005);
        staticPaint.setColor(color);
        staticPaint.setAlpha(128);
        staticPaint.setStyle(Paint.Style.STROKE);
        staticPaint.setStrokeWidth(staticWidth);

        dynamicCX = staticCX;
        dynamicCY = staticCY;
        dynamicRadius = (float) (staticRadius * 0.35);
        dynamicWidth = (float) (staticWidth * 0.6);
        dynamicPaint.setColor(color);
        dynamicPaint.setAlpha(200);
        dynamicPaint.setStyle(Paint.Style.STROKE);
        dynamicPaint.setStrokeWidth(dynamicWidth);

        interval = 15;

        post(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(staticCX, staticCY, staticRadius, staticPaint);
        canvas.drawCircle(dynamicCX, dynamicCY, dynamicRadius, dynamicPaint);
    }

    @Override
    public void OnOrientationChanged(Vector2f degree) {
        this.degree = degree;
    }

    @Override
    public void run() {
        dynamicCX = staticCX - height * (degree.x / 90f);
        dynamicCY = staticCY + width * (degree.y / 90f);

        invalidate();
        postDelayed(this, interval);
    }
}
