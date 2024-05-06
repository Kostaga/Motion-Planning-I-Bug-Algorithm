// MyRobot.java
package bug;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static bug.Helpers.circumNavigate;

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
        intensity1 = intensity2  = intensity3 = 0;
        foundLocalMax = false;
    }
    public void performBehavior()
    {
        double llIntensity = Math.pow(leftLight.getLux(),0.1);
        double rlIntensity = Math.pow(rightLight.getLux(),0.1);
        double clIntensity = Math.pow(centerLight.getLux(),0.1);

        intensity1 = intensity2;
        intensity2 = intensity3;
        intensity3 = clIntensity;

        boolean bumped = false;
        iL = bumped ? 1 : 0;
        for (int i=0;i<sonars.getNumSensors();i++) {
            if (sonars.getMeasurement(i) < 1) {
                bumped = true;
                iH = clIntensity;
                System.out.println("Bumper " + i + " has hit");
            }
        }

        if (intensity2 > intensity1 && intensity2 > intensity3)
            foundLocalMax = true;



            if (1 - clIntensity < 0.202) {
                setRotationalVelocity(0);
                setTranslationalVelocity(0);
            }
            else if (foundLocalMax) {
                System.out.println("MPHKAAA");
                if (Math.abs(rlIntensity - llIntensity) > 0.0001) {
                    setRotationalVelocity((llIntensity - rlIntensity) * 10);
                    System.out.println("if1");
                } else if (clIntensity > llIntensity) {
                    setRotationalVelocity(2);
                    System.out.println("if2");
                } else {
                    System.out.println("if3");
                    setTranslationalVelocity(1);
                }
            }
            else if (bumped) {
                circumNavigate(this, sonars, false);
            } else {
                if (Math.abs(rlIntensity - llIntensity) > 0.0001) {
                    setRotationalVelocity((llIntensity - rlIntensity) * 10);
                    System.out.println("if1");
                } else if (clIntensity > llIntensity) {
                    setRotationalVelocity(2);
                    System.out.println("if2");
                } else {
                    System.out.println("if3");
                    setTranslationalVelocity(1);
                }
            }



    }
}
