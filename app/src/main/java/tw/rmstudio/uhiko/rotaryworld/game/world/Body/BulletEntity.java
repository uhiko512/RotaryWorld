package tw.rmstudio.uhiko.rotaryworld.game.world.Body;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.game.player.MovementManager;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.ObjModel;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

/**
 * Created by uhiko on 14/12/25.
 */
public class BulletEntity implements SimulationBody {
    private RigidBody rigidBody;

    private OnTransformListener listener;

    private float range;
    private Vector3f position, initPosistion, initShift, initVelocity;

    public BulletEntity(Transform transform) {
        initShift = new Vector3f(-5, 0, 0);
        transform.basis.transform(initShift);
        transform.origin.add(initShift);

        initBody(transform);

        initPosistion = (Vector3f) transform.origin.clone();
        position = (Vector3f) initPosistion.clone();
        range = 150;
        initVelocity = new Vector3f(-350, 0, 0);
        transform.basis.transform(initVelocity);
    }

    private void initBody(Transform transform) {
        CollisionShape shape = new SphereShape(1);

        float mass = 1.0f;
        Vector3f localInertia = new Vector3f(0, 0, 0);
        shape.calculateLocalInertia(mass, localInertia);

        DefaultMotionState motionState = new DefaultMotionState(transform);
        rigidBody = new RigidBody(mass, motionState, shape, localInertia);
        rigidBody.setUserPointer(this);
        Collision.addBody(rigidBody);
    }

    public boolean isInRange() {
        return new Point3f(this.position).distance(new Point3f(initPosistion)) <= range;
    }

    public void setOnTransformListener(OnTransformListener listener) {
        this.listener = listener;
    }

    public void distroy() {
        GLESRenderer.removeObjModel((ObjModel) listener);
        Collision.removeBody(rigidBody);
        MovementManager.removeSmulationBody(this);
    }

    @Override
    public void toMove() {
        if (isInRange()) {
            rigidBody.setLinearVelocity(initVelocity);
        }
    }

    @Override
    public void moved() {
        if (isInRange()) {
            Transform transform = new Transform();
            rigidBody.getMotionState().getWorldTransform(transform);
            transform.origin.get(position);

            float[] openGLMatrix = new float[16];
            transform.getOpenGLMatrix(openGLMatrix);
            if (listener != null) {
                listener.onTransform(new Transform(transform), openGLMatrix);
            }
        } else {
            distroy();
        }
    }
}
