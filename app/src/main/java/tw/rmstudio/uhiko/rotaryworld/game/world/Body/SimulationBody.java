package tw.rmstudio.uhiko.rotaryworld.game.world.Body;

import com.bulletphysics.linearmath.Transform;

/**
 * Created by uhiko on 14/12/16.
 */
public interface SimulationBody {
    public void toMove();
    public void moved();

    public interface OnTransformListener {
        public void onTransform(Transform transform, float[] modelMatrix);
    }
}
