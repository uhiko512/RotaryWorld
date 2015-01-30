package tw.rmstudio.uhiko.rotaryworld.game;

import android.opengl.GLSurfaceView;

import com.bulletphysics.linearmath.Transform;

import tw.rmstudio.uhiko.rotaryworld.game.player.MovementManager;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.BulletShape;
import tw.rmstudio.uhiko.rotaryworld.game.world.Body.BulletEntity;

/**
 * Created by uhiko on 14/12/25.
 */
public class Bullet implements Runnable {
    BulletEntity entity;
    BulletShape shape;

    public Bullet(GLSurfaceView glView, Transform transform) {
        entity = new BulletEntity(transform);
        shape = new BulletShape(glView.getContext());

        entity.setOnTransformListener(shape);
        MovementManager.addSmulationBody(entity);
        glView.queueEvent(this);
    }

    @Override
    public void run() {
        GLESRenderer.addObjModel(shape);
    }
}
