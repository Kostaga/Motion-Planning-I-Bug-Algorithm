// MyRobot.java
package bug;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import static bug.Helpers.circumNavigate;
import static bug.Helpers.turnAround;
import static bug.Helpers.goForward;
import static bug.Helpers.orientate;
import static bug.Helpers.chooseAction;
import static bug.Helpers.stopRobot;

public class MyRobot extends Agent {
    LightSensor centerLight,rightLight,leftLight;
    RangeSensorBelt bumpers, sonars;

    double intensity1, intensity2, intensity3, iL, iH;

    boolean foundLocalMax;



    public MyRobot (Vector3d position, String name) {
        super(position,name);
        leftLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,-0.6), 0, "left");
        rightLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,0.6), 0, "right");
        centerLight = RobotFactory.addLightSensor(this, new Vector3d(0,0.47,0), 0, "center");
        bumpers = RobotFactory.addBumperBeltSensor(this,8);
        sonars = RobotFactory.addSonarBeltSensor(this,8);
    }
    public void initBehavior() {
        setTranslationalVelocity(0);
        setRotationalVelocity(0);
        intensity1 = intensity2  = intensity3 = 0;
        foundLocalMax = false;
    }
    public void performBehavior()
    {
        double llIntensity = Math.pow(leftLight.getLux(),0.1);
        double rlIntensity = Math.pow(rightLight.getLux(),0.1);
        double clIntensity = Math.pow(centerLight.getLux(),0.1);

        // Finding the local maximum using 3 intensities (page 3 ik−1 > ik−2 and ik−1 > ik)
        intensity1 = intensity2;
        intensity2 = intensity3;
        intensity3 = clIntensity;


        boolean bumped = false;
        // 1) Let iL = ht(x)
        iL = bumped ? 1 : 0;
        for (int i=0;i<sonars.getNumSensors();i++) {
            if (sonars.getMeasurement(i) < 1) {
                bumped = true;
                if (iL != clIntensity)
                    iH = clIntensity;
                System.out.println("Bumper " + i + " has hit");
            }
        }


        // Check if a local maximum is found
        if (intensity2 > intensity1 && intensity2 > intensity3 && intensity3 > iL) {
            foundLocalMax = true;
            System.out.println("Local maximum found");
        }

            // 4) If iL != hi(x), then let iH = hi(x)
//            if (iL != clIntensity) {
//                iH = clIntensity;
//            }

        // 3) If hi(x) ~= 1, then stop
        if (1 - clIntensity < 0.202) {
            stopRobot(this);
        }
        else if (bumped ) {
            circumNavigate(this, sonars, false);
        }
        else {
            chooseAction(this, clIntensity, llIntensity, rlIntensity);
        }



    }
}
