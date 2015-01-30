package tw.rmstudio.uhiko.rotaryworld;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by uhiko on 15/1/1.
 */
public class AudioMameger {
    private Context context;

    private static AudioMameger uniqueInstance;
    private HashMap<Class, Vector<MediaPlayer>> audioGroups;

    private AudioMameger(Context context) {
        this.context = context;

        audioGroups = new HashMap<Class, Vector<MediaPlayer>>();

        audioGroups.put(MainMenu.class, createAudioGroup(context, new int[] {
                R.raw.main_menu_bgm
        }));

        audioGroups.put(PracticeScene.class, createAudioGroup(context, new int[] {
                R.raw.scene_bgm,
                R.raw.wind_bgs,
                R.raw.airplane_propeller
        }));
    }

    private Vector<MediaPlayer> createAudioGroup(Context context, int[] audioIds) {
        Vector<MediaPlayer> audioGroup = new Vector<MediaPlayer>();
        for (int id : audioIds) {
            audioGroup.addElement(MediaPlayer.create(context, id));
        }

        return audioGroup;
    }

    public void start(Class groupClass) {
        for (MediaPlayer audio : audioGroups.get(groupClass)) {
            audio.start();
            audio.setLooping(true);
        }
    }

    public void start(Class groupClass, int index) {
        MediaPlayer audio = audioGroups.get(groupClass).elementAt(index);
        audio.start();
        audio.setLooping(true);
    }

    public void pause(Class groupClass) {
        for (MediaPlayer audio : audioGroups.get(groupClass)) {
            audio.pause();
        }
    }

    public void pause(Class groupClass, int index) {
        audioGroups.get(groupClass).elementAt(index).pause();
    }

    public void pauseAll() {
        for (Class groupClass : audioGroups.keySet()) {
            pause(groupClass);
        }
    }

    public void restart(Class groupClass) {
        for (MediaPlayer audio : audioGroups.get(groupClass)) {
            audio.seekTo(0);
        }
    }

    public void stop(Class groupClass) {
        for (MediaPlayer audio : audioGroups.get(groupClass)) {
            audio.stop();
        }
    }

    public void stopAll() {
        for (Class groupClass : audioGroups.keySet()) {
            stop(groupClass);
        }
    }

    public static AudioMameger getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new AudioMameger(context);
        }

        return uniqueInstance;
    }
}
