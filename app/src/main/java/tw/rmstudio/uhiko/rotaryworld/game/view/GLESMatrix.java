package tw.rmstudio.uhiko.rotaryworld.game.view;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import static tw.rmstudio.uhiko.rotaryworld.game.util.Axis.X;
import static tw.rmstudio.uhiko.rotaryworld.game.util.Axis.Y;
import static tw.rmstudio.uhiko.rotaryworld.game.util.Axis.Z;

/**
 * Created by uhiko on 14/12/9.
 */
public abstract class GLESMatrix {
    protected static float[] viewMatrix = new float[16];
    protected static float[] modelMatrix = new float[16];
    protected static float[] projectionMatrix = new float[16];
    protected static float[] MVMatrix = new float[16];
    protected static float[] MVPMatrix = new float[16];

    protected static void setViewMatrix(Vector3f eye, Vector3f look, Vector3f up) {
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z, look.x, look.y, look.z, up.x, up.y, up.z);
    }

    protected static void setProjectionMatrix(int width, int height) {
        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 1000f);
    }

    public static void setModelMatrix(float[] modelMatrix) {
        Matrix.multiplyMM(GLESMatrix.modelMatrix, 0, modelMatrix, 0, GLESMatrix.modelMatrix, 0);
    }

    public static void glslMatrixHandle(int mvMatrixHandle, int mvpMatrixHandle) {
        Matrix.multiplyMM(MVMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, MVMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, MVPMatrix, 0);
    }

    public static void glslVertexHandle(float[] vertex, int vertexHandle) {
        float[] mvVertex = new float[4];
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMV(mvVertex, 0, modelMatrix, 0, vertex, 0);
        Matrix.multiplyMV(mvVertex, 0, viewMatrix, 0, mvVertex, 0);
        GLES20.glUniform3f(vertexHandle, mvVertex[X], mvVertex[Y], mvVertex[Z]);
    }

    public static void setIdentity() {
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public static Transform transformClone(Transform transform) {
        Transform t = new Transform();
        t.basis.set((Matrix3f) transform.basis.clone());
        t.origin.set((Vector3f) transform.origin.clone());
        //Log.e("s", String.valueOf(t.equals(transform)));
        return t;
    }

    public static void translate(Vector3f vector) {
        Matrix.translateM(modelMatrix, 0, vector.x, vector.y, vector.z);
    }

    public static void rotate (float angle, float... vector) {
        Matrix.rotateM(modelMatrix, 0, angle, vector[X], vector[Y], vector[Z]);
    }

    public static void rotate(Matrix3f rotateM3) {
        Matrix4f rotateM4 = new Matrix4f();
        float[] rotateF4 = new float[32];
        rotateM4.set(rotateM3);
        for (int i = 0; i < 16; i++) {
            rotateF4[i] = rotateM4.getElement(i / 4, i % 4);
        }

        Matrix.multiplyMM(rotateF4, 16, modelMatrix, 0, rotateF4, 0);
        System.arraycopy(rotateF4, 16, modelMatrix, 0, 16);
    }

    public static void scale(float... vector) {
        if (vector.length == 1) {
            Matrix.scaleM(modelMatrix, 0, vector[X], vector[X], vector[X]);
        } else {
            Matrix.scaleM(modelMatrix, 0, vector[X], vector[Y], vector[Z]);
        }

    }
}
