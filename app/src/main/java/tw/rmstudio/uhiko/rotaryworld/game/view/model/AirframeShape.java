package tw.rmstudio.uhiko.rotaryworld.game.view.model;

import android.content.Context;

import tw.rmstudio.uhiko.rotaryworld.R;

import static tw.rmstudio.uhiko.rotaryworld.game.util.FileLoader.objLoad;

/**
 * Created by uhiko on 14/12/2.
 */
public class AirframeShape extends ObjModel {

    public AirframeShape(Context context) {
        super(objLoad(context, R.raw.aircraft_a_obj));
    }
}
