package tw.rmstudio.uhiko.rotaryworld.game.util;

import android.content.Context;
import android.opengl.GLES20;

import java.util.concurrent.ConcurrentHashMap;

import tw.rmstudio.uhiko.rotaryworld.game.datatype.GLSLProgram;

/**
 * Created by uhiko on 14/11/28.
 */
public class GLSLCompiler {

    public static GLSLProgram build(Context context, int[] codeIds, String[] handleNames) {
        int[] shaderType = new int[]{GLES20.GL_VERTEX_SHADER, GLES20.GL_FRAGMENT_SHADER};

        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            for (int i = 0; i < codeIds.length; i++) {
                String code = FileLoader.getFileText(context, codeIds[i], "\n");


                int shaderHandle = GLES20.glCreateShader(shaderType[i]);
                final int[] compileStatus = new int[1];
                if (shaderHandle != 0) {
                    GLES20.glShaderSource(shaderHandle, code);
                    GLES20.glCompileShader(shaderHandle);
                    GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
                    if (compileStatus[0] == 0) {
                        GLES20.glDeleteShader(shaderHandle);
                        shaderHandle = 0;
                    }
                } else {
                    throw new RuntimeException("Error creating vertex shader.");
                }

                GLES20.glAttachShader(programHandle, shaderHandle);
            }

            GLES20.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        } else {
            throw new RuntimeException("Error creating program.");
        }

        ConcurrentHashMap<String, Integer> handle = new ConcurrentHashMap<String, Integer>();
        for (String name : handleNames) {
            if (name.startsWith("u_")) {
                handle.put(name, GLES20.glGetUniformLocation(programHandle, name));
            } else if (name.startsWith("a_")) {
                handle.put(name, GLES20.glGetAttribLocation(programHandle, name));
            }
        }

        return new GLSLProgram(programHandle, handle);
    }
}
