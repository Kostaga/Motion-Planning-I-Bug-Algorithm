// MyRobot.java
package bug;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import static bug.Helpers.circumNavigate;
import static bug.Helpers.goForward;
import static bug.Helpers.orientate;
//import static bug.Helpers.chooseAction;
import static bug.Helpers.stopRobot;

public class MyRobot extends Agent {
    LightSensor centerLight,rightLight,leftLight;
    RangeSensorBelt sonars;

    double intensity1;
    double intensity2;
    double intensity3;

    double iL;
    double iH;

    double GOAL = 0.784;


    public enum RobotStatus {
        ORIENTATION,
        FORWARD,
        FOLLOWING,
        END
    }

    public static RobotStatus status;



    public MyRobot (Vector3d position, String name) {
        super(position,name);
        leftLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,-0.6), 0, "left");
        rightLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,0.6), 0, "right");
        centerLight = RobotFactory.addLightSensor(this, new Vector3d(0,0.47,0), 0, "center");
        sonars = RobotFactory.addSonarBeltSensor(this,8);
        status = RobotStatus.ORIENTATION;

    }


    public RobotStatus getStatus() {
        return MyRobot.status;
    }
    public void initBehavior() {
        intensity1 = intensity2  = intensity3 = 0;
        iL = iH = 0;
    }
    public void performBehavior()
    {
        double llIntensity = Math.pow(leftLight.getLux(),0.1);
        double rlIntensity = Math.pow(rightLight.getLux(),0.1);
        double clIntensity = Math.pow(centerLight.getLux(),0.1);

        // Finding the local maximum using 3 intensities (page 3 ik−1 > ik−2 and ik−1 > ik)
        intensity1 = intensity2;
        intensity2 = intensity3;
        intensity3 = Math.round(clIntensity*100000.0)/100000.0;


        if (clIntensity > GOAL) {
            status = RobotStatus.END;
        }
        switch (status) {
            case ORIENTATION:
                iL = clIntensity;
//                System.out.println("Orientate");
                orientate(this, llIntensity, rlIntensity, clIntensity);
                break;
            case FORWARD:
//                System.out.println("Forward");

                goForward(this, sonars);
                clIntensity = Math.pow(centerLight.getLux(),0.1);
                if (iL != clIntensity) {
                    iH = clIntensity;
                }
                break;
            case FOLLOWING:
//                System.out.println("Following");
                circumNavigate(this, sonars, false);
                if (clIntensity > iH && intensity3 < intensity2 && intensity2 > intensity1 && clIntensity > iL)
                    status = RobotStatus.ORIENTATION;

                break;

            case END:
                stopRobot(this);
                break;
        }


    }
}
