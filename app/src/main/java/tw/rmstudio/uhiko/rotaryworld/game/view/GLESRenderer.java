package tw.rmstudio.uhiko.rotaryworld.game.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

import tw.rmstudio.uhiko.rotaryworld.game.player.Camera;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.ObjModel;

import static tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix.setProjectionMatrix;
import static tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix.setViewMatrix;

/**
 * Created by uhiko on 2014/9/16.
 */
public class GLESRenderer implements GLSurfaceView.Renderer, Camera.OnMoveListener {
    private Context context;

    private static Vector<ObjModel> objModels;
    public static boolean isSurfaceCreated;

    public GLESRenderer(Context context) {
        this.context = context;
        objModels = new Vector<ObjModel>();
        isSurfaceCreated = false;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        isSurfaceCreated = true;
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        GLES20.glClearColor(230 / 255f, 244 / 255f, 255 / 255f, 0.7f);

        ObjModel.programHandle(context);
        for (ObjModel obj : objModels) {
            obj.createBufferObject();
        }

        //lightProgram = GLSLCompiler.build(context, new int[]{R.raw.light_vertex_shader, R.raw.light_fragment_shader}, new String[]{"u_MVPMatrix", "a_Position"});
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        setProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < objModels.size(); i++) {
            objModels.elementAt(i).draw();
        }
    }

    @Override
    public void onMove(Vector3f eye, Vector3f look, Vector3f up) {
        setViewMatrix(eye, look, up);
    }

    public static void addObjModel(ObjModel... objs) {
        for (ObjModel obj : objs) {
            if (isSurfaceCreated) {
                obj.createBufferObject();
            }
            objModels.addElement(obj);
        }
    }

    public static void removeObjModel(ObjModel... objs) {
        for (ObjModel obj : objs) {
            objModels.removeElement(obj);
        }
    }

    public static void removeAllObjModel() {
        objModels.removeAllElements();
    }
}
