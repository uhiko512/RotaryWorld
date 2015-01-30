package tw.rmstudio.uhiko.rotaryworld.game.view.controller;

import android.content.Context;
import android.graphics.Paint;

import java.util.Timer;
import java.util.TimerTask;

import tw.rmstudio.uhiko.rotaryworld.SoundManeger;

import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.height;
import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.width;

/**
 * Created by uhiko on 14/11/27.
 */
public class Accelerator extends Controller {
    private Timer timer;

    private boolean isAccelerate;
    private long interval;
    private OnAccelerateListener listener;

    private float acceleration;

    public Accelerator(Context context) {
        super(context);
        timer.schedule(new AccelerateLoop(), 0, interval);
    }

    @Override
    public void paramsInit() {
        timer = new Timer();
        paint = new Paint();

        cx = (float) (height * 0.1);
        cy = (float) (width * 0.85);
        radius = (float) (height * 0.055);
        defaultColor = 0xff6fff00;
        touchColor = 0xffb6ff7d;
        interval = 100;
        acceleration = 0.2f;
    }

    @Override
    public void actionDown() {
        if (listener != null) {
            isAccelerate = true;
            soundManeger.play(SoundManeger.SoundId.acceleration, false);
        }
        paint.setColor(touchColor);
    }

    @Override
    public void actionUp() {
        if (listener != null) {
            isAccelerate = false;
            listener.onStop();
        }
        paint.setColor(defaultColor);
    }

    public void setOnAccelerateListener(OnAccelerateListener listener) {
        this.listener = listener;
    }


    public interface OnAccelerateListener {
        public void onAccelerate(float acceleration);
        public void onStop();
    }

    private class AccelerateLoop extends TimerTask {

        @Override
        public void run() {
            if (isAccelerate) {
                listener.onAccelerate(acceleration);
            }
        }
    }
}
