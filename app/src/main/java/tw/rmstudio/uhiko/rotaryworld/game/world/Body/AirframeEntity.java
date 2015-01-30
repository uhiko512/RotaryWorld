package tw.rmstudio.uhiko.rotaryworld.game.world.Body;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

import java.util.Vector;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.game.datatype.ObjData;
import tw.rmstudio.uhiko.rotaryworld.game.player.OrientationSensor;
import tw.rmstudio.uhiko.rotaryworld.game.view.controller.Accelerator;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

/**
 * Created by uhiko on 14/12/18.
 */
public class AirframeEntity implements SimulationBody, OrientationSensor.OnOrientationChangedListener, Accelerator.OnAccelerateListener {
    private RigidBody rigidBody;

    private Vector<OnTransformListener> listeners;

    private float initVelocity, velocityChange, maxAngularVelocity;
    private Vector2f degree, angularVelocity;
    private Vector3f position;
    private Matrix3f modelMatrix;

    private CradleHead camera;

    public AirframeEntity(ObjData data) {
        listeners = new Vector<OnTransformListener>();

        initVelocity = 5f;
        maxAngularVelocity = 5f;
        degree = new Vector2f();
        angularVelocity = new Vector2f(0, 0);
        position = new Vector3f(0, 1, 0);
        modelMatrix = new Matrix3f();


        ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();

        for (float[] v : data.getVertices().elementAt(0)) {
            vertices.add(new Vector3f(v));
        }

        initBody(vertices);
    }

    private void initBody( ObjectArrayList<Vector3f> vertices) {
        //CollisionShape shape = new SphereShape(1);

        CollisionShape shape = new ConvexHullShape(vertices);

        float mass = 1.0f;
        Vector3f localInertia = new Vector3f(0, 0, 0);
        shape.calculateLocalInertia(mass, localInertia);

        DefaultMotionState motionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), position, 1.0f)));
        rigidBody = new RigidBody(mass, motionState, shape, localInertia);
        Collision.addBody(rigidBody);
    }

    private void rotate() {
        angularVelocity = new Vector2f((float) Math.pow(degree.x / 90, 2) * Math.copySign(1, degree.x), (float) Math.pow(degree.y / 90, 2) * Math.copySign(1, degree.y));
        angularVelocity.scale(maxAngularVelocity);
        angularVelocity.negate();
    }

    public void addOnTransformListener(OnTransformListener listener) {
        listeners.addElement(listener);
    }

    public void setCamera(CradleHead camera) {
        this.camera = camera;
    }

    @Override
    public void toMove() {
        Vector3f realSpeed = new Vector3f(-(initVelocity + velocityChange), 0, 0);
        Vector3f realAngle = new Vector3f(0, angularVelocity.x, angularVelocity.y);

        rotate();
        modelMatrix.transform(realSpeed);
        modelMatrix.transform(realAngle);
        rigidBody.setLinearVelocity(realSpeed);
        rigidBody.setAngularVelocity(realAngle);
    }

    @Override
    public void moved() {
        Transform transform = new Transform();
        rigidBody.getMotionState().getWorldTransform(transform);
        transform.origin.get(position);
        camera.linkPosition((Vector3f) position.clone());

        this.modelMatrix = (Matrix3f) transform.basis.clone();
        camera.linkModelMatrix(modelMatrix);

        float[] openGLMatrix = new float[16];
        transform.getOpenGLMatrix(openGLMatrix);
        for (OnTransformListener listener : listeners) {
            listener.onTransform(new Transform(transform), openGLMatrix);
        }
    }

    @Override
    public void OnOrientationChanged(Vector2f degree) {
        this.degree = degree;
        this.degree.negate();
    }

    @Override
    public void onAccelerate(float acceleration) {
        velocityChange += acceleration;
    }

    @Override
    public void onStop() {
        velocityChange = 0;
    }


    public interface CradleHead {
        public void linkPosition(Vector3f position);
        public void linkModelMatrix(Matrix3f modelMatrix);
    }
}
