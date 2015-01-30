package tw.rmstudio.uhiko.rotaryworld;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.HashMap;

import tw.rmstudio.uhiko.rotaryworld.game.util.Screen;


public class GLESActivity extends Activity {
    private HashMap<Class, Fragment> pages;
    private FrameLayout container;
    private AudioMameger audioMameger;
    private SoundManeger soundManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Screen.setSize(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

        audioMameger = AudioMameger.getInstance(this);
        soundManeger = SoundManeger.getInstance(this);

        container = new FrameLayout(this);
        container.setId(container.hashCode());

        pages = new HashMap<Class, Fragment>();
        pages.put(MainMenu.class, new MainMenu());
        pages.put(PracticeScene.class, new PracticeScene());

        changePage(MainMenu.class);

        setContentView(container);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioMameger.stopAll();
        soundManeger.stopAll();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getFragmentManager().popBackStack();
        }

        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        }

        return false;
    }

    public boolean isTopFragment(Class fragmentClass) {
        return pages.get(fragmentClass).equals(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1));
    }

    public void changePage(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(container.getId(), fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void changePage(Class fragmentClass) {
        changePage(pages.get(fragmentClass));
    }

    /*public native String stringFromJNI();

    static {
        System.loadLibrary("objLoader");
    }*/
}
