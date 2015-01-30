package tw.rmstudio.uhiko.rotaryworld.game.world;

import android.content.Context;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.util.ObjectArrayList;

import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.SoundManeger;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.Cube;

/**
 * Created by uhiko on 14/11/27.
 */
public class Collision {
    private static Collision uniqueInstance;
    private static DiscreteDynamicsWorld dynamicsWorld;
    private static SoundManeger soundManeger;

    private Collision(Context context) {
        soundManeger = SoundManeger.getInstance(context);
        initPhysics();
    }

    public void initPhysics() {
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        BroadphaseInterface broadphase = new DbvtBroadphase();
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3f(0, 0, 0));
    }

    public static void addBody(RigidBody body) {
        dynamicsWorld.addRigidBody(body);
    }

    public static void addBody(RigidBody body, short group, short mask) {
        dynamicsWorld.addRigidBody(body, group, mask);
    }

    public static void removeBody(RigidBody body) {
        body.destroy();
        dynamicsWorld.removeRigidBody(body);
    }

    public static void removeAllBody() {
        for (CollisionObject object : dynamicsWorld.getCollisionObjectArray()) {
            dynamicsWorld.removeCollisionObject(object);
        }
    }

    public static void stepSimulation() {
        dynamicsWorld.stepSimulation(1 / 60f, 10);

        ObjectArrayList<PersistentManifold> manifolds = dynamicsWorld.getDispatcher().getInternalManifoldPointer();
        for (PersistentManifold manifold : manifolds) {
            CollisionObject objA, objB;
            Object pointerA, pointerB;
            objA = (CollisionObject) manifold.getBody0();
            objB = (CollisionObject) manifold.getBody1();
            pointerA = objA.getUserPointer();
            pointerB = objB.getUserPointer();

            for (int i = 0; i < manifold.getNumContacts(); i++) {
                ManifoldPoint point = manifold.getContactPoint(i);
                if (point.getDistance() < 0.f) {
                    if (pointerB instanceof Cube) {
                        ((Cube) objB.getUserPointer()).distroy();
                        soundManeger.play(SoundManeger.SoundId.explosion, false);
                    } else if (pointerA instanceof Cube) {
                        ((Cube) objA.getUserPointer()).distroy();
                        soundManeger.play(SoundManeger.SoundId.explosion, false);
                    }
                }
            }
        }
    }

    public static Collision getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new Collision(context);
        }

        return uniqueInstance;
    }
}
