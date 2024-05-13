package bug;
import javax.vecmath.Vector3d;
import simbad.sim.CherryAgent;
import simbad.sim.EnvironmentDescription;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Vector;
import javax.vecmath.*;
import simbad.gui.*;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    Env(){
        this.light1IsOn = true; // Goal Light
        this.light1Position = new Vector3d(0,2,-3);
        this.light2IsOn = false;
        this.ambientLightColor = new Color3f(0,0,0);
        spiral(this);
//        spheres(this);
//        bottle(this);
//        box(this);
        add(new MyRobot(new Vector3d(-6, 0, 5), "robot 1"));
    }


    // Spheres Object
    static void spheres(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,3), new Vector3f(5,1,5),environment));
        environment.add(new Box(new Vector3d(2.75,0,3), new Vector3f(0.5f,1,4),environment));
        environment.add(new Box(new Vector3d(3.25,0,3), new Vector3f(0.5f,1,3),environment));
        environment.add(new Box(new Vector3d(-2.75,0,3), new Vector3f(0.5f,1,4),environment));
        environment.add(new Box(new Vector3d(-3.25,0,3), new Vector3f(0.5f,1,3),environment));

        environment.add(new Box(new Vector3d(0,0,-4), new Vector3f(2,1,2),environment));
    }


    // Box Object
    static void box(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,0), new Vector3f(5,1,5),environment));
    }

    // Bottle Object
    static void bottle(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,3), new Vector3f(5,1,5),environment));
        environment.add(new Box(new Vector3d(0,0,-0.5), new Vector3f(2,1,2),environment));
    }


    // Spiral Object
    static void spiral(EnvironmentDescription environment){
        environment.add(new Box(new Vector3d(0,0,7), new Vector3f(14,1,1),environment));
        environment.add(new Box(new Vector3d(-2,0,3), new Vector3f(12,1,1),environment));
        environment.add(new Box(new Vector3d(0,0,-9), new Vector3f(16,1,1),environment));
        environment.add(new Box(new Vector3d(-7.5,0,-2.5), new Vector3f(1,1,12),environment));
        environment.add(new Box(new Vector3d(7.5,0,-0.5), new Vector3f(1,1,16),environment));
        environment.add(new Box(new Vector3d(3.5,0,-1), new Vector3f(1,1,7),environment));
        environment.add(new Box(new Vector3d(0,0,-5), new Vector3f(7,1,1),environment));
        environment.add(new Box(new Vector3d(-3.5,0,-2.5), new Vector3f(1,1,4),environment));
        environment.add(new Box(new Vector3d(-1.5,0,-1), new Vector3f(3,1,1),environment));
    }
}




