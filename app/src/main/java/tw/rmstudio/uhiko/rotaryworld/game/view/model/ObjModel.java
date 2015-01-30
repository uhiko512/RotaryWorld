package tw.rmstudio.uhiko.rotaryworld.game.view.model;

import android.content.Context;
import android.opengl.GLES20;

import com.bulletphysics.linearmath.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import tw.rmstudio.uhiko.rotaryworld.R;
import tw.rmstudio.uhiko.rotaryworld.game.datatype.GLSLProgram;
import tw.rmstudio.uhiko.rotaryworld.game.datatype.ObjData;
import tw.rmstudio.uhiko.rotaryworld.game.util.GLSLCompiler;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix;
import tw.rmstudio.uhiko.rotaryworld.game.world.Body.SimulationBody;

import static tw.rmstudio.uhiko.rotaryworld.game.util.Unit.BYTES_PER_FLOAT;
import static tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix.glslMatrixHandle;
import static tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix.glslVertexHandle;
import static tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix.setIdentity;

/**
 * Created by uhiko on 14/12/1.
 */
public abstract class ObjModel implements SimulationBody.OnTransformListener {
    public static int COORDS_PER_VERTEX = 3;
    public static int COORDS_PER_TEXTURE = 2;
    public static int COORDS_PER_NORMAL = 3;
    public static int[] COORDS_PER_ELEMENT = new int[] {COORDS_PER_VERTEX, COORDS_PER_TEXTURE, COORDS_PER_NORMAL};

    public ObjData objData;

    private static GLSLProgram program;

    private Vector<Vector<float[]>> vertices;
    private Vector<Vector<Short>> indices;
    private FloatBuffer[] elementBuffer;

    private int[] vboId = new int[3];

    protected float[] modelMatrix;

    private float[] lightVertex;

    public ObjModel(ObjData data) {
        objData = data;
        vertices = data.getVertices();
        indices = data.getIndices();

        elementBuffer = new FloatBuffer[3];

        lightVertex = new float[] {0.0f, 50.0f, 0.0f, 1.0f};

        parseBuffer();
    }

    private void parseBuffer() {
        for (int i = 0; i < elementBuffer.length; i++) {
            ByteBuffer byteBuff = ByteBuffer.allocateDirect(indices.elementAt(i).size() * BYTES_PER_FLOAT * COORDS_PER_ELEMENT[i]);
            byteBuff.order(ByteOrder.nativeOrder());
            elementBuffer[i] = byteBuff.asFloatBuffer();

            for (int index : indices.elementAt(i)) {
                float[] coord = vertices.elementAt(i).elementAt(index);

                for (int axis = 0; axis < coord.length; axis++) {
                    elementBuffer[i].put(coord[axis]);
                }
            }
            elementBuffer[i].position(0);
        }
    }

    public void createBufferObject() {
        GLES20.glGenBuffers(3, vboId, 0);

        for (int i = 0; i < vboId.length; i++) {
            if (i != 1) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[i]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, elementBuffer[i].capacity() * BYTES_PER_FLOAT, elementBuffer[i], GLES20.GL_STATIC_DRAW);
            }
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    private void sendToGLSL() {
        int positionHandle = program.getHandle("a_Position");
        int colorHandle = program.getHandle("a_Color");
        int normalHandle = program.getHandle("a_Normal");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[0]);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(positionHandle);

        /*GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[1]);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(colorHandle);*/

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[2]);
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(normalHandle);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void draw() {
        program.use();
        sendToGLSL();
        glslVertexHandle(lightVertex, program.getHandle("u_LightPos"));
        setIdentity();
        setModelMatrix();
        glslMatrixHandle(program.getHandle("u_MVMatrix"), program.getHandle("u_MVPMatrix"));
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, indices.elementAt(0).size());
    }

    public static void programHandle(Context context) {
        program = GLSLCompiler.build(context,
                        new int[]{R.raw.shape_vertex_shader, R.raw.shape_fragment_shader},
                        new String[]{"u_MVPMatrix", "u_MVMatrix", "u_LightPos", "a_Position", "a_Color", "a_Normal"}
        );
    }


    public ObjData getObjData() {
        return objData;
    }

    protected void setModelMatrix() {
        if (modelMatrix != null) {
            GLESMatrix.setModelMatrix(modelMatrix);
        }
    }

    @Override
    public void onTransform(Transform transform, float[] modelMatrix) {
        this.modelMatrix = modelMatrix;
    }
}
