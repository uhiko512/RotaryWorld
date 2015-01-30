package tw.rmstudio.uhiko.rotaryworld;

import android.app.Fragment;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tw.rmstudio.uhiko.rotaryworld.game.Airframe;
import tw.rmstudio.uhiko.rotaryworld.game.TargetMameger;
import tw.rmstudio.uhiko.rotaryworld.game.player.Camera;
import tw.rmstudio.uhiko.rotaryworld.game.player.MovementManager;
import tw.rmstudio.uhiko.rotaryworld.game.player.OrientationSensor;
import tw.rmstudio.uhiko.rotaryworld.game.view.Crosshairs;
import tw.rmstudio.uhiko.rotaryworld.game.view.GLESRenderer;
import tw.rmstudio.uhiko.rotaryworld.game.view.ScoreBar;
import tw.rmstudio.uhiko.rotaryworld.game.view.controller.Accelerator;
import tw.rmstudio.uhiko.rotaryworld.game.view.controller.Trigger;
import tw.rmstudio.uhiko.rotaryworld.game.view.model.Wall;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

/**
 * Created by uhiko on 14/12/7.
 */
public class PracticeScene extends Fragment implements Runnable {
    private AudioMameger audioMameger;

    private OrientationSensor sensor;
    private Camera camera;
    private Collision collision;
    private MovementManager movementManager;
    private Airframe airframe;

    private Wall wall;
    private TargetMameger targetMameger;

    private FrameLayout orthoLayout;
    private GLSurfaceView gameView;
    private GLESRenderer renderer;
    private Accelerator accelerator;
    private Crosshairs crosshairs;
    private Trigger trigger;
    private ScoreBar scoreBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();

        camera.setOnMoveListener(renderer);
        gameView.setEGLContextClientVersion(2);
        gameView.setRenderer(renderer);
        GLESRenderer.addObjModel(wall);

        new Handler().postDelayed(this, 150);

        return orthoLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioMameger = AudioMameger.getInstance(getActivity());

        airframe = new Airframe(gameView);
        targetMameger = new TargetMameger(gameView);

        sensor.addOnOrientationChangedListener(airframe.getEntity(), crosshairs);
        accelerator.setOnAccelerateListener(airframe.getEntity());
        trigger.setOnFireListener(airframe);
        targetMameger.setOnHitListener(scoreBar);
        targetMameger.setOnCompleteListener(scoreBar);
        airframe.getEntity().setCamera(camera);
    }

    public void init() {
        Context context = getActivity();

        gameView = new GLSurfaceView(context);
        renderer = new GLESRenderer(context);

        sensor = new OrientationSensor(context);

        collision = Collision.getInstance(context);
        camera = new Camera();
        movementManager = new MovementManager();


        wall = new Wall(context);

        orthoLayout = new FrameLayout(context);
        accelerator = new Accelerator(context);
        crosshairs = new Crosshairs(context);
        trigger = new Trigger(context);
        scoreBar = new ScoreBar(context);

        orthoLayout.addView(gameView);
        orthoLayout.addView(accelerator);
        orthoLayout.addView(crosshairs);
        orthoLayout.addView(trigger);
        orthoLayout.addView(scoreBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        audioMameger.start(this.getClass());
    }

    @Override
    public void onPause() {
        super.onPause();
        audioMameger.pause(this.getClass());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioMameger.restart(this.getClass());
        MovementManager.removeAllSmulationBody();
        Collision.removeAllBody();
        GLESRenderer.removeAllObjModel();
    }

    @Override
    public void run() {
        sensor.initInitialAngle();
    }
}
