package tw.rmstudio.uhiko.rotaryworld.game;

import android.opengl.GLSurfaceView;

import tw.rmstudio.uhiko.rotaryworld.game.player.BulletManager;
import tw.rmstudio.uhiko.rotaryworld.game.player.MovementManager;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.controller.Trigger;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.AirframeShape;
import tw.rmstudio.uhiko.rotaryworld.game.world.Body.AirframeEntity;

/**
 * Created by uhiko on 14/12/18.
 */
public class Airframe implements Trigger.OnFireListener, Runnable {
    private AirframeEntity entity;
    private AirframeShape shape;

    private BulletManager bulletManager;

    public Airframe(final GLSurfaceView glView) {
        shape = new AirframeShape(glView.getContext());
        entity = new AirframeEntity(shape.getObjData());

        bulletManager = new BulletManager(glView);

        entity.addOnTransformListener(shape);
        entity.addOnTransformListener(bulletManager);

        MovementManager.addSmulationBody(entity);
        glView.queueEvent(this);
    }

    public AirframeEntity getEntity() {
        return entity;
    }

    public AirframeShape getShape() {
        return shape;
    }

    @Override
    public void run() {
        GLESRenderer.addObjModel(shape);
    }

    @Override
    public void onFire() {
        bulletManager.onFire();
    }

    @Override
    public void stopFire() {
        bulletManager.stopFire();
    }
}
