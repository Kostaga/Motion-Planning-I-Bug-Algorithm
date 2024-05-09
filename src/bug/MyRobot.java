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
//import static bug.Helpers.chooseAction;
import static bug.Helpers.stopRobot;

public class MyRobot extends Agent {
    LightSensor centerLight,rightLight,leftLight;
    RangeSensorBelt bumpers, sonars;

    double intensity1;
    double intensity2;
    double intensity3;
    double iL;
    double iH;

    boolean foundLocalMax;

    public enum RobotStatus {
        ORIENTATION,
        FORWARD,
        FOLLOWING,
        END;
    }

    public static RobotStatus status;



    public MyRobot (Vector3d position, String name) {
        super(position,name);
        leftLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,-0.6), 0, "left");
        rightLight = RobotFactory.addLightSensor(this, new Vector3d(0.6,0.47,0.6), 0, "right");
        centerLight = RobotFactory.addLightSensor(this, new Vector3d(0,0.47,0), 0, "center");
        bumpers = RobotFactory.addBumperBeltSensor(this,8);
        sonars = RobotFactory.addSonarBeltSensor(this,8);
        status = RobotStatus.ORIENTATION;

    }

    public void setiL(double iL) {
        this.iL = iL;
    }

    public double getiL() {
        return this.iL;
    }
    public void setiH(double iH) {
        this.iH = iH;
    }
    public double getiH() {
        return this.iH;
    }


    public RobotStatus getStatus() {
        return MyRobot.status;
    }
    public void initBehavior() {
        intensity1 = intensity2  = intensity3 = 0;
        foundLocalMax = false;
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
        intensity3 = clIntensity;



        if (1 - clIntensity < 0.202) {
            status = RobotStatus.END;
        }
        switch (status) {
            case ORIENTATION:
                System.out.println("Orientate");
                orientate(this, llIntensity, rlIntensity, clIntensity);
                break;
            case FORWARD:
                System.out.println("Forward");
                goForward(this, sonars,clIntensity);

                break;
            case FOLLOWING:
                System.out.println("Following");
                circumNavigate(this, sonars, false, clIntensity);
                if (clIntensity > iH && intensity2 > intensity1 && intensity2 > intensity3) {
                    MyRobot.status = RobotStatus.ORIENTATION;
                    setiL(0);
                }
                break;

            case END:
                stopRobot(this);
                break;
        }
     //   iL = clIntensity;


//        boolean bumped = false;
//        // 1) Let iL = ht(x)
//        iL = bumped ? 1 : 0;
//        for (int i=0;i<sonars.getNumSensors();i++) {
//            if (sonars.getMeasurement(i) < 1) {
//                bumped = true;
//                if (iL != clIntensity)
//                    iH = clIntensity;
//                System.out.println("Bumper " + i + " has hit");
//            }
//        }
//
//
//        // Check if a local maximum is found
//        if (intensity2 > intensity1 && intensity2 > intensity3 && intensity3 > iL) {
//            foundLocalMax = true;
//            System.out.println("Local maximum found");
//        }
//
//            // 4) If iL != hi(x), then let iH = hi(x)
////            if (iL != clIntensity) {
////                iH = clIntensity;
////            }
//
//        // 3) If hi(x) ~= 1, then stop
//        if (1 - clIntensity < 0.202) {
//            stopRobot(this);
//        }
//        else if (bumped ) {
//            circumNavigate(this, sonars, false);
//        }
//        else {
//            chooseAction(this, clIntensity, llIntensity, rlIntensity);
//        }



    }
}
