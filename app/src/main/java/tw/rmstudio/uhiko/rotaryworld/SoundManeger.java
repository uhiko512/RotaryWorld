package tw.rmstudio.uhiko.rotaryworld;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.Vector;

/**
 * Created by uhiko on 15/1/2.
 */
public class SoundManeger {
    private static SoundManeger uniqueInstance;

    private int[] soundResIds;
    private SoundPool soundPool;
    private Vector<Integer[]> soundIds;

    private SoundManeger(Context context) {
        soundResIds = new int[] {R.raw.menu_decision_true, R.raw.automatic_gun, R.raw.acceleration, R.raw.explosion};
        soundPool = new SoundPool(soundResIds.length, AudioManager.STREAM_MUSIC, 5);
        soundIds = new Vector<Integer[]>();
        for (int id : soundResIds) {
            soundIds.addElement(new Integer[] {soundPool.load(context, id, 5), 0});
        }
    }

    public static SoundManeger getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new SoundManeger(context);
        }

        return uniqueInstance;
    }

    public void play(SoundId id, boolean isLoop) {
        soundIds.elementAt(id.ordinal())[1] = soundPool.play(soundIds.elementAt(id.ordinal())[0], 1.0f, 1.0f, 0, isLoop ? -1 : 0, 1.0f);
    }

    public void stop(SoundId id) {
        soundPool.stop(soundIds.elementAt(id.ordinal())[1]);
    }

    public void stopAll() {
        for (Integer[] id : soundIds) {
            if (id[1] != 0) {
                soundPool.stop(id[1]);
            }
        }
    }

    public enum SoundId {
        menu_decision_true,
        automatic_gun,
        acceleration,
        explosion
    }
}
