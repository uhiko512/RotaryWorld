package tw.rmstudio.uhiko.rotaryworld.game.player;

import java.util.Timer;
import java.util.TimerTask;

import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.game.world.Body.AirframeEntity;

/**
 * Created by uhiko on 14/11/25.
 */
public class Camera implements AirframeEntity.CradleHead {
    private Timer timer;
    private OnMoveListener onMovelistener;

    private long interval;

    private float overlook;
    private Vector3f eye, look, up, position;
    private Matrix3f modelMatrix;

    public Camera() {
        timer = new Timer();

        interval = 1000 / 70;

        overlook = 3;

        position = new Vector3f();
        modelMatrix = new Matrix3f();

        timer.schedule(new FollowLoop(), 0, interval);
    }

    public void setOnMoveListener(OnMoveListener listener) {
        onMovelistener = listener;
    }

    public void follow() {
        eye = new Vector3f(5, overlook, 0);
        up = new Vector3f(0, 1, 0);
        look = new Vector3f(-10, overlook, 0);

        Quat4f quat4f = new Quat4f();
        quat4f.set(modelMatrix);
        quat4f.x = 0;
        quat4f.y = 0;
        Matrix3f m = new Matrix3f();
        m.set(quat4f);
        modelMatrix.transform(eye);
        modelMatrix.transform(up);
        modelMatrix.transform(look);
        eye.add(position);
        look.add(position);
    }

    @Override
    public void linkPosition(Vector3f position) {
        this.position = position;
    }

    @Override
    public void linkModelMatrix(Matrix3f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }


    public interface OnMoveListener {
        public void onMove(Vector3f eye, Vector3f look, Vector3f up);
    }


    private class FollowLoop extends TimerTask {

        @Override
        public void run() {
            follow();

            if (onMovelistener != null) {
                onMovelistener.onMove(eye, look, up);
            }
        }
    }

}
