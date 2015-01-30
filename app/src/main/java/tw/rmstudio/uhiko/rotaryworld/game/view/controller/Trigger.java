package tw.rmstudio.uhiko.rotaryworld.game.view.controller;

import android.content.Context;
import android.graphics.Paint;

import tw.rmstudio.uhiko.rotaryworld.SoundManeger;

import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.height;
import static tw.rmstudio.uhiko.rotaryworld.game.util.Screen.width;

/**
 * Created by uhiko on 14/12/14.
 */
public class Trigger extends Controller {
    private OnFireListener listener;

    public Trigger(Context context) {
        super(context);
    }

    @Override
    public void paramsInit() {
        paint = new Paint();

        cx = (float) (height * 0.9);
        cy = (float) (width * 0.85);
        radius = (float) (height * 0.055);
        defaultColor = 0xffff4d00;
        touchColor = 0xffffa37b;
    }

    @Override
    public void actionDown() {
        if (listener != null) {
            soundManeger.play(SoundManeger.SoundId.automatic_gun, true);
            listener.onFire();
        }

        paint.setColor(touchColor);
    }

    @Override
    public void actionUp() {
        if (listener != null) {
            soundManeger.stop(SoundManeger.SoundId.automatic_gun);
            listener.stopFire();
        }

        paint.setColor(defaultColor);
    }

    public void setOnFireListener(OnFireListener listener) {
        this.listener = listener;
    }


    public interface OnFireListener {
        public void onFire();
        public void stopFire();
    }
}
