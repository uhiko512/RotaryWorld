package tw.rmstudio.uhiko.rotaryworld.game.player;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Vector;

import javax.vecmath.Vector2f;

/**
 * Created by uhiko on 14/11/24.
 */
public class OrientationSensor implements SensorEventListener {
    private boolean isInit;
    private float[] initAngle, angle;

    private SensorManager sensorManager;
    private Sensor aSensor, mSensor;

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    private Vector<OnOrientationChangedListener> listeners;

    public OrientationSensor(Context context) {
        initAngle = new float[3];
        angle = new float[3];

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);

        listeners = new Vector<OnOrientationChangedListener>();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] RM = new float[9];

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldValues = sensorEvent.values;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = sensorEvent.values;
                break;
        }

        SensorManager.getRotationMatrix(RM, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(RM, angle);


        for (int i = 0; i < angle.length; i++) {
            angle[i] = (float) Math.toDegrees(angle[i]);
            if (isInit) {
                angle[i] -= initAngle[i];
            }
        }

        Vector2f degree = new Vector2f(angle[1], angle[2]);
        for (OnOrientationChangedListener l : listeners) {
            l.OnOrientationChanged((Vector2f) degree.clone());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    public void addOnOrientationChangedListener(OnOrientationChangedListener... listeners) {
        for (OnOrientationChangedListener l : listeners) {
            this.listeners.addElement(l);
        }
    }


    public interface OnOrientationChangedListener {
        public void OnOrientationChanged(Vector2f degree);
    }

    public void initInitialAngle() {
        for (int i = 0; i < initAngle.length; i++) {
            initAngle[i] = angle[i];
        }
        isInit = true;
    }
}
