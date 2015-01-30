package tw.rmstudio.uhiko.rotaryworld.game.player;

import android.opengl.GLSurfaceView;

import com.bulletphysics.linearmath.Transform;

import java.util.Timer;
import java.util.TimerTask;

import tw.rmstudio.uhiko.rotaryworld.game.Bullet;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.controller.Trigger;
import tw.rmstudio.uhiko.rotaryworld.game.world.Body.AirframeEntity;

/**
 * Created by uhiko on 14/12/15.
 */
public class BulletManager implements Trigger.OnFireListener, AirframeEntity.OnTransformListener {
    private GLSurfaceView glView;
    private Transform transform;

    private Timer timer;

    private boolean isFire;

    public BulletManager(GLSurfaceView glView) {
        this.glView = glView;
        timer = new Timer();
        isFire = false;

        timer.schedule(new BulletLoop(), 0, 200);
    }

    @Override
    public void onFire() {
        isFire = true;
    }

    @Override
    public void stopFire() {
        isFire = false;
    }

    @Override
    public void onTransform(Transform transform, float[] modelMatrix) {
        this.transform = transform;
    }


    private class BulletLoop extends TimerTask {

        @Override
        public void run() {
            if (isFire && GLESRenderer.isSurfaceCreated) {
                new Bullet(glView, transform);
            }
        }
    }
}
