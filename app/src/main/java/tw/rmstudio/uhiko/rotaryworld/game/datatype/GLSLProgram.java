package tw.rmstudio.uhiko.rotaryworld.game.datatype;

import android.opengl.GLES20;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by uhiko on 14/12/2.
 */
public class GLSLProgram {
    protected int program;
    protected ConcurrentHashMap<String, Integer> handle;

    public GLSLProgram(int program, ConcurrentHashMap<String, Integer> handle) {
        this.program = program;
        this.handle = handle;
    }

    public void use() {
        GLES20.glUseProgram(program);
    }

    public int getHandle(String name) {
        return handle.get(name);
    }
}
