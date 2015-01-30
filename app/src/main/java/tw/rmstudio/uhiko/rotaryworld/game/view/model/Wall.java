package tw.rmstudio.uhiko.rotaryworld.game.view.model;

import android.content.Context;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.R;
import tw.rmstudio.uhiko.rotaryworld.game.datatype.ObjData;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

import static tw.rmstudio.uhiko.rotaryworld.game.util.FileLoader.objLoad;

/**
 * Created by uhiko on 14/12/14.
 */
public class Wall extends ObjModel {
    private ObjData obj;

    public Wall(Context context) {
        super(objLoad(context, R.raw.wall_obj));

        obj = objLoad(context, R.raw.wall_obj);

        ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();

        for (float[] v : obj.getVertices().elementAt(0)) {
            vertices.add(new Vector3f(v));
        }

        initBody(vertices);
    }

    @Override
    protected void setModelMatrix() { }

    public void initBody(ObjectArrayList<Vector3f> vertices) {
        CollisionShape groundShape = new ConvexHullShape(vertices);
        DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1.0f)));
        RigidBody groundRigidBody = new RigidBody(0, groundMotionState, groundShape, new Vector3f(0,0,0));
        groundRigidBody.setUserPointer(this);

        Collision.addBody(groundRigidBody);
    }
}

