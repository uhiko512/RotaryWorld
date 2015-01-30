package tw.rmstudio.uhiko.rotaryworld.game;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.bulletphysics.linearmath.Transform;

import java.util.Timer;
import java.util.TimerTask;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.Cube;

/**
 * Created by uhiko on 14/12/29.
 */
public class TargetMameger implements Runnable {
    private Timer timer;
    private long interval;

    private Cube cube;
    private GLSurfaceView glView;

    private OnHitListener onHitListener;
    private OnCompleteListener onCompleteListener;

    private long lastMillis;
    private int hitNumber;
    private int maxTargetNumber;

    public TargetMameger(GLSurfaceView glView) {
        this.glView = glView;
        timer = new Timer();
        interval = 1000;

        lastMillis = SystemClock.uptimeMillis();
        hitNumber = 0;
        maxTargetNumber = 1;

        createCube();
        timer.schedule(new TargetLoop(), 0, interval);
    }

    private void createCube() {
        glView.queueEvent(this);
        Cube.isExist = true;
    }

    private float angleRandom() {
        return (float) (Math.random() * 360);
    }

    private float positionRandom(float range) {
        return (float) (Math.random() * range * 2 - range);
    }

    private long calcTimeSpent() {
        return SystemClock.uptimeMillis() - lastMillis;
    }

    public void setOnHitListener(OnHitListener listener) {
        onHitListener = listener;
        listener.onHit(maxTargetNumber, hitNumber, 0);
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    @Override
    public void run() {
        Quat4f angle = new Quat4f(angleRandom(), angleRandom(), angleRandom(), angleRandom());
        Vector3f position = new Vector3f(positionRandom(15), positionRandom(5) + 5, positionRandom(15));

        cube = new Cube(glView.getContext(), new Transform(new Matrix4f(angle, position, 1.0f)));
        GLESRenderer.addObjModel(cube);
    }

    private class TargetLoop extends TimerTask {

        @Override
        public void run() {
            if (!Cube.isExist) {
                hitNumber++;
                if (onHitListener != null && hitNumber > 0) {
                    onHitListener.onHit(maxTargetNumber, hitNumber, calcTimeSpent());
                }

                if (hitNumber < maxTargetNumber) {
                    createCube();
                } else {
                    if (onCompleteListener != null) {
                        onCompleteListener.onComplete();
                    }
                    timer.cancel();
                }
            }
        }
    }

    public interface OnHitListener {
        public void onHit(int maxTargetNumber, int hitNumber, long timeSpent);
    }

    public interface OnCompleteListener {
        public void onComplete();
    }
}
