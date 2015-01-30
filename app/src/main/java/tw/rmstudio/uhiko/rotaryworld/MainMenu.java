package tw.rmstudio.uhiko.rotaryworld;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

import tw.rmstudio.uhiko.rotaryworld.game.util.Screen;

/**
 * Created by uhiko on 14/12/7.
 */
public class MainMenu extends Fragment implements Button.OnClickListener {
    private LinearLayout mainLayout, modeLayout;
    private TextView title;
    private Vector<Button> buttons;

    private String[] buttonStrs;
    private ViewGroup.LayoutParams wrapContent;
    private AudioMameger audioMameger;
    private SoundManeger soundManeger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buttonStrs = new String[] {"Practice Mode", "Story Mode", "Multiplayer Mode"};
        wrapContent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        audioMameger = AudioMameger.getInstance(getActivity());
        soundManeger = SoundManeger.getInstance(getActivity());

        mainLayout = new LinearLayout(getActivity());
        modeLayout = new LinearLayout(getActivity());

        title = new TextView(getActivity());

        buttons = new Vector<Button>();
        for (String buttonStr : buttonStrs) {
            Button button = new Button(getActivity());
            button.setText(buttonStr);
            button.setOnClickListener(this);
            buttons.addElement(button);
            modeLayout.addView(button);
        }

        title.setText("Rotary World");
        title.setTextSize(50);
        title.setLayoutParams(wrapContent);
        modeLayout.setLayoutParams(wrapContent);
        setCenter(title);
        setCenter(modeLayout);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(title);
        mainLayout.addView(modeLayout);

        buttons.elementAt(1).setEnabled(false);
        buttons.elementAt(2).setEnabled(false);

        return mainLayout;
    }

    private void setCenter(View view) {
        view.measure(0, 0);
        view.setX((Screen.height - view.getMeasuredWidth()) / 2);
    }

    @Override
    public void onStart() {
        super.onStart();
        audioMameger.start(this.getClass());
    }

    @Override
    public void onPause() {
        super.onPause();
        audioMameger.pause(this.getClass());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioMameger.restart(this.getClass());
    }

    @Override
    public void onClick(View view) {
        soundManeger.play(SoundManeger.SoundId.menu_decision_true, false);
        ((GLESActivity) getActivity()).changePage(PracticeScene.class);
    }
}
