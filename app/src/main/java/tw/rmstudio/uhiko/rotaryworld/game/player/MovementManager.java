package tw.rmstudio.uhiko.rotaryworld.game.player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import tw.rmstudio.uhiko.rotaryworld.game.world.Body.SimulationBody;
import tw.rmstudio.uhiko.rotaryworld.game.world.Collision;

/**
 * Created by uhiko on 14/12/16.
 */
public class MovementManager {
    private Timer timer;
    private long interval;

    private static Vector<SimulationBody> simulationBodies;

    public MovementManager() {
        timer = new Timer();
        simulationBodies = new Vector<SimulationBody>();

        interval = 1000 / 70;

        timer.schedule(new MovementLoop(), 0, interval);
    }

    public static void addSmulationBody(SimulationBody body) {
        simulationBodies.addElement(body);
    }

    public static void removeSmulationBody(SimulationBody body) {
        simulationBodies.removeElement(body);
    }

    public static void removeAllSmulationBody() {
        simulationBodies.removeAllElements();
    }

    private class MovementLoop extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i < simulationBodies.size(); i++) {
                simulationBodies.elementAt(i).toMove();
            }

            try {
                Collision.stepSimulation();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < simulationBodies.size(); i++) {
                simulationBodies.elementAt(i).moved();
            }
        }
    }
}
