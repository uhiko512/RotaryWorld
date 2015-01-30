package tw.rmstudio.uhiko.rotaryworld.game.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import tw.rmstudio.uhiko.rotaryworld.game.datatype.ObjData;

/**
 * Created by uhiko on 14/11/30.
 */
public class FileLoader {

    public static String getFileText(InputStream inputStream, String nlRegExp) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String nextLine;
        StringBuilder strBuff = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                strBuff.append(nextLine + nlRegExp);
            }
        }
        catch (IOException e) {
            return null;
        }

        return strBuff.toString();
    }

    public static String getFileText(Context context ,int resourceId, String nlRegExp) {
        return getFileText(context.getResources().openRawResource(resourceId), nlRegExp);
    }

    public static ObjData objLoad(Context context, int resourceId) {
        Vector<Vector<float[]>> vertices;
        Vector<Vector<Short>> indices;

        String obj = getFileText(context, resourceId, "\n");
        String[] strLines = obj.split("\n");

        String[] prefix = {"v ", "vt ", "vn ", "f "};
        vertices = new Vector<Vector<float[]>>();
        indices = new Vector<Vector<Short>>();
        for (int i = 0; i < 3; i++) {
            vertices.addElement(new Vector<float[]>());
            indices.addElement(new Vector<Short>());
        }

        for (String strLine : strLines) {
            for (int i = 0; i < prefix.length; i++) {
                if (strLine.startsWith(prefix[i])) {
                    String[] coord = strLine.replace(prefix[i], "").split(" ");
                    if (i != 3) {
                        float[] axises = i == 1 ? new float[2] : new float[3];
                        for (int j = 0; j < coord.length; j++) {
                            axises[j] = Float.valueOf(coord[j]);
                        }
                        vertices.elementAt(i).addElement(axises);
                    } else {
                        for (String index : coord) {
                            for (int j = 0; j < 3; j++) {
                                indices.elementAt(j).addElement((short) (Integer.valueOf(index.split("/")[j]) - 1));
                            }
                        }

                    }
                }
            }
        }

        return new ObjData(vertices, indices);
    }
}
