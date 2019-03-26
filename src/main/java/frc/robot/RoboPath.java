package frc.robot;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class RoboPath{
    Pathfinder instance;
    Trajectory rocket;
    Trajectory left, right;
    double wheelbase_width = 0.7239; //meters
    double distance = 5.334; //meters
    double wheel_diameter = 0.1524; //meters
    double angleDiff = 0.0;

    TankModifier modifier;
    EncoderFollower right1, left1;

    public void generateRocketTraj(){
        DriveTrain.resetAHRS();
        Waypoint[] points = new Waypoint[] {
          new Waypoint(0, 0, 0),
          new Waypoint(2, 2, Pathfinder.d2r(45))
        };
        
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory rocket = Pathfinder.generate(points, config);
    
        TankModifier modifier = new TankModifier(rocket).modify(wheelbase_width);
    
        Trajectory left = modifier.getLeftTrajectory();
        Trajectory right = modifier.getRightTrajectory();

        left1 = new EncoderFollower(left);
        right1 = new EncoderFollower(right);

        left1.reset();
        left1.configureEncoder(DriveTrain.getLeftEncPos(), 1, wheel_diameter);
        left1.configurePIDVA(1.0, 0.0, 0.0, 1 / 5, 0);

        right1.configureEncoder(DriveTrain.getRightEncPos(), 1, wheel_diameter);
        right1.configurePIDVA(1.0, 0.0, 0.0, 1 / 5, 0);

    }

    public double[] getPower(){
        double l = left1.calculate(DriveTrain.getLeftEncPos());
        double r = right1.calculate(DriveTrain.getRightEncPos());
    
        double gyro_heading = DriveTrain.getAHRS();  // Assuming the gyro is giving a value in degrees
        double desired_heading = Pathfinder.r2d(left1.getHeading());  // Should also be in degrees
    
        // This allows the angle difference to respect 'wrapping', where 360 and 0 are the same value
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        angleDifference = angleDifference % 360.0;
        if (Math.abs(angleDifference) > 180.0) {
          angleDiff = (angleDifference > 0) ? angleDifference - 360 : angleDiff + 360;
        } 
    
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        System.out.println("LEFT: " + ((l+turn)/3));
        System.out.println("RIGHT: " + ((r-turn)/3));

        double[] power = {l+turn, r-turn};
        return power;
        //DriveTrain.drive((l+turn)/3, (r-turn)/3);
    }
}