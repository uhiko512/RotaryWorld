package tw.rmstudio.uhiko.rotaryworld.game.util;

import com.bulletphysics.linearmath.QuaternionUtil;

import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Created by uhiko on 14/11/25.
 */
public class Axis {
    public static int X = 0;
    public static int Y = 1;
    public static int Z = 2;

    public static float step3D(float[] angle, int axis, float length, boolean isOverlook) {
        float radianX = (float) Math.toRadians(angle[X]);
        float radianY = (float) Math.toRadians(isOverlook ? (90 - Math.abs(angle[Y] % 360) * (angle[Y] > 0 ? -1 : 1)) : angle[Y]);

        switch (axis) {
            case 0:
                return (float) ((length * Math.cos(radianY)) * Math.cos(radianX));
            case 1:
                return (float) (length * Math.sin(radianY));
            case 2:
                return (float) ((length * Math.cos(radianY)) * Math.sin(radianX));
        }

        return -1;
    }

    public static float[] step3D(float[] angle, float length, boolean isOverlook) {
        float[] step = new float[3];

        for (int axis = X; axis <= Z; axis++) {
            step[axis] = step3D(angle, axis, length, isOverlook);
        }

        return step;
    }

    public static void transform(Vector2f angle, Vector3f... vectors) {
        Quat4f roll = new Quat4f();
        Quat4f yaw = new Quat4f();
        Quat4f pitch = new Quat4f();
        QuaternionUtil.setRotation(roll, new Vector3f(1, 0, 0), (float) Math.toRadians(0));
        QuaternionUtil.setRotation(yaw, new Vector3f(0, 1, 0), (float) Math.toRadians(angle.x));
        QuaternionUtil.setRotation(pitch, new Vector3f(0, 0, 1), (float) Math.toRadians(angle.y));
        roll.mul(yaw, pitch);

        Matrix3f modelMat = new Matrix3f();
        modelMat.set(roll);

        for (Vector3f vector : vectors) {
            modelMat.transform(vector);
        }
    }
}
