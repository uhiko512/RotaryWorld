package tw.rmstudio.uhiko.rotaryworld.game.datatype;

import java.util.Vector;

/**
 * Created by uhiko on 14/12/7.
 */
public class ObjData {
    private Vector<Vector<float[]>> vertices;
    private Vector<Vector<Short>> indices;

    public ObjData(Vector<Vector<float[]>> vertices, Vector<Vector<Short>> indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public Vector<Vector<float[]>> getVertices() {
        return vertices;
    }

    public Vector<Vector<Short>> getIndices() {
        return indices;
    }
}
