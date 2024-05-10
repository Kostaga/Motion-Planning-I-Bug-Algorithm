package bug;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


public class Helpers {
    static double K1 = 3;
    static double K2 = 0.8;
    static double K3 = 1;

    static double SAFETY = 0.8;

    public static void goForward(MyRobot rob, RangeSensorBelt sonars, double centerIntensity){

        rob.setTranslationalVelocity(3);

        if (rob.getiL() != centerIntensity)
            rob.setiH(centerIntensity);
        if (bumped(sonars)) {
            MyRobot.status = MyRobot.RobotStatus.FOLLOWING;
            rob.setiL(1);
        }

    }

    public static boolean bumped( RangeSensorBelt sonars){

        int[] hits = {0,1,6,7};
        for (int i : hits) {
            if (sonars.getMeasurement(i) < 1) {
                return true;
            }
        }

        return false;
    }

    public static void orientate(MyRobot rob, double leftIntensity, double rightIntensity, double centerIntensity){
        rob.setTranslationalVelocity(0);


        if (Math.abs(rightIntensity - leftIntensity) > 0.001) {
            rob.setRotationalVelocity(Math.signum(leftIntensity - rightIntensity) * 0.5);
        }
        else if (centerIntensity > leftIntensity) {
            rob.setRotationalVelocity(2);
        }
        else {
            rob.setRotationalVelocity(0);
            MyRobot.status = MyRobot.RobotStatus.FORWARD;
        }


    }


    public static void stopRobot(MyRobot rob) {
        rob.setRotationalVelocity(0);
        rob.setTranslationalVelocity(0);
    }


    public static Point3d getSensedPoint(MyRobot rob,RangeSensorBelt sonars,int sonar){
      double v;
      if(sonars.hasHit(sonar))
        v=rob.getRadius()+sonars.getMeasurement(sonar);
      else
        v=rob.getRadius()+sonars.getMaxRange();
      double x=v*Math.cos(sonars.getSensorAngle(sonar));
      double z=v*Math.sin(sonars.getSensorAngle(sonar));
      return new Point3d(x,0,z);
    }
    public static double wrapToPi(double a){
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }
    public static void circumNavigate(MyRobot rob, RangeSensorBelt sonars, boolean CLOCKWISE, double centerIntensity){
        int min;
        min=0;
        for (int i=1;i<sonars.getNumSensors();i++)
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
                min=i;
        Point3d p = getSensedPoint(rob,sonars,min);
        double d = p.distance(new Point3d(0,0,0));
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot);

        rob.setRotationalVelocity(K1*phRef);
        rob.setTranslationalVelocity(K2*Math.cos(phRef));


    }
}
