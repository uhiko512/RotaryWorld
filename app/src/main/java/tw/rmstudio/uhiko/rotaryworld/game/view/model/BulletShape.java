package tw.rmstudio.uhiko.rotaryworld.game.view.model;

import android.content.Context;

import tw.rmstudio.uhiko.rotaryworld.R;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESMatrix;

import static tw.rmstudio.uhiko.rotaryworld.game.util.FileLoader.objLoad;

/**
 * Created by uhiko on 14/12/14.
 */
public class BulletShape extends ObjModel {

    public BulletShape(Context context) {
        super(objLoad(context, R.raw.bullet_obj));
    }

    @Override
    protected void setModelMatrix() {
        GLESMatrix.scale(3.5f);
        super.setModelMatrix();
    }
}
