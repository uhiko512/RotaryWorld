package tw.rmstudio.uhiko.rotaryworld.game.view.model;

import android.content.Context;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.R;
import tw.rmstudio.uhiko.rotaryworld.game.datatype.ObjData;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

import static tw.rmstudio.uhiko.rotaryworld.game.util.FileLoader.objLoad;

/**
 * Created by uhiko on 14/11/26.
 */
public class Cube extends ObjModel {
    public static boolean isExist;

    private ObjData obj;
    private RigidBody rigidBody;

    public Cube(Context context, Transform transform) {
        super(objLoad(context, R.raw.cube_obj));

        obj = objLoad(context, R.raw.cube_obj);

        ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();

        for (float[] v : obj.getVertices().elementAt(0)) {
            vertices.add(new Vector3f(v));
        }

        initBody(vertices, transform);
    }

    public void initBody(ObjectArrayList<Vector3f> vertices, Transform transform) {
        CollisionShape shape = new ConvexHullShape(vertices);
        DefaultMotionState motionState = new DefaultMotionState(transform);
        rigidBody = new RigidBody(0, motionState, shape, new Vector3f(0, 0, 0));
        rigidBody.setUserPointer(this);

        Transform t = new Transform();
        rigidBody.getMotionState().getWorldTransform(t);
        modelMatrix = new float[16];
        t.getOpenGLMatrix(modelMatrix);

        Collision.addBody(rigidBody);
    }

    public void distroy() {
        GLESRenderer.removeObjModel(this);
        Collision.removeBody(rigidBody);
        isExist = false;
    }
}

