package tw.rmstudio.uhiko.rotaryworld.game.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import tw.rmstudio.uhiko.rotaryworld.AudioMameger;
import tw.rmstudio.uhiko.rotaryworld.PracticeScene;
import tw.rmstudio.uhiko.rotaryworld.R;
import tw.rmstudio.uhiko.rotaryworld.game.TargetMameger;
import tw.rmstudio.uhiko.rotaryworld.game.util.Screen;

/**
 * Created by uhiko on 14/12/29.
 */
public class ScoreBar extends View implements TargetMameger.OnHitListener, TargetMameger.OnCompleteListener, Runnable {
    private Timer timer;

    private String number;
    private int score, wholeScore, dynamicScore;
    long maxTime;

    private int numberTextSize, scoreTextSize;

    private Paint numberPaint, scorePaint, barPaint;

    private AlertDialog.Builder resultDialog;

    private AudioMameger audioMameger;
    private MediaPlayer audio;

    public ScoreBar(Context context) {
        super(context);

        timer = new Timer();

        numberPaint = new Paint();
        scorePaint = new Paint();
        barPaint = new Paint();

        resultDialog = new AlertDialog.Builder(getContext());
        audio = MediaPlayer.create(context, R.raw.mission_complete_bgm);

        audioMameger = AudioMameger.getInstance(getContext());

        number = "";
        score = 0;
        wholeScore = 10000;
        dynamicScore = 0;
        maxTime = 10000;
        numberTextSize = 40;
        scoreTextSize = 30;
        numberPaint.setTextSize(numberTextSize);
        numberPaint.setColor(Color.DKGRAY);
        numberPaint.setTextAlign(Paint.Align.LEFT);
        scorePaint.setTextSize(scoreTextSize);
        scorePaint.setColor(Color.DKGRAY);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        barPaint.setColor(Color.LTGRAY);
        barPaint.setAlpha(165);

        resultDialog.setMessage("Mission Complete");
        resultDialog.setPositiveButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                audio.stop();
                audioMameger.start(PracticeScene.class, 0);
            }
        });

        //timer.schedule(new DynamicScoreLoop(), 0, 300);
    }

    @Override
    public void onHit(int maxTargetNumber, int hitNumber, long timeSpent) {
        number = hitNumber + " / " + maxTargetNumber;
        if (timeSpent != 0 && timeSpent < maxTime && timeSpent > 10) {
            score += wholeScore * (1 - ((double) timeSpent / maxTime));
        }
        post(this);
    }

    @Override
    public void onComplete() {
        post(new Runnable() {
            @Override
            public void run() {
                audio.start();
                audioMameger.pause(PracticeScene.class, 0);
                resultDialog.show();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, Screen.height, numberTextSize + 10, barPaint);
        canvas.drawText(number, 0, numberTextSize, numberPaint);
        canvas.drawText("Score: " + score, Screen.height * 0.85f, scoreTextSize, scorePaint);
    }

    @Override
    public void run() {
        invalidate(0, 0, Screen.height, numberTextSize);
    }

    private class DynamicScoreLoop extends TimerTask {
        private int addValue = 73;

        @Override
        public void run() {
            if (dynamicScore < score) {
                dynamicScore += score - dynamicScore < addValue ? score - dynamicScore : addValue;
                post(ScoreBar.this);
            }
        }
    }
}
