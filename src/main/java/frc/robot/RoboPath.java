package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Pathfinder.PathfinderJNIException;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class RoboPath implements Runnable{
    Pathfinder instance;
    Trajectory rocket;
    Trajectory left, right;
    double wheelbase_width = 0.7239; //meters
    double distance = 5.334; //meters
    double wheel_diameter = 0.1524; //meters
    double angleDiff = 0.0;

    TankModifier modifier;
    EncoderFollower right1, left1, rocket1;
    public static boolean isDone = false;

    public void setup() throws IOException{
        left = PathfinderFRC.getTrajectory("Test.right");
        right = PathfinderFRC.getTrajectory("Test.left");
        
        //rocket1 = new EncoderFollower(rocket);
        left1 = new EncoderFollower(left);
        right1 = new EncoderFollower(right);
    
        //left1.reset();
        left1.configureEncoder((int) DriveTrain.getEncoderLeft(), 26, wheel_diameter);
        left1.configurePIDVA(0.0, 0.0, 0.0, 1d / 15d, 0);
    
        right1.configureEncoder((int) DriveTrain.getEncoderRight(), 26, wheel_diameter);
        right1.configurePIDVA(0.0, 0.0, 0.0, 1d / 15d, 0);
    }

    @Override
    public void run(){
        if (left1.isFinished() && right1.isFinished()) {
            
            DriveTrain.drive(0d, 0d);
           
            RoboPath.isDone = true;

          } else {
            double left_speed = left1.calculate((int) DriveTrain.getEncoderLeft());
            double right_speed = right1.calculate((int) DriveTrain.getEncoderRight());
            double heading = DriveTrain.getAHRS();
            double desired_heading = -Pathfinder.r2d(left1.getHeading());
            double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
      
            
            SmartDashboard.putNumber("HYRO: ", DriveTrain.getAHRS());
            double turn =  0.1 * (-1.0/80.0) * heading_difference;
            System.out.println("LEFT: " +  (left_speed + turn));
            System.out.println("RIGHT: " + (right_speed - turn));

            //https://www.chiefdelphi.com/t/problems-with-pathfinder-motion-profiling/163830/3
            DriveTrain.drive((left_speed + turn), -(right_speed - turn));
        } 
    }
}
