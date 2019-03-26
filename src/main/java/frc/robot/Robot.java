/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;


public class Robot extends TimedRobot{
  private static final String ROBOT_FILE_PATH = "/home/lvuser/config/config.json";
  Trajectory rocket;
  Trajectory left;
  Trajectory right;
  public static double wheelbase_width = 0.7239; //meters
  public static double distance = 5.334; //meters
  public static double wheel_diameter = 0.1524; //meters
  //public TankModifier modifier;
  public RoboPath rocketPath;

  @Override
  public void robotInit() {
    DriveTrain.getInstance();
    TeleOp.getInstance();
    Elevator.getInstance();
    Ingestor.getInstance();
    Diagnostics.getInstance();
    LEDs.getInstance();

    TeleOp.init();
    Elevator.setPosition(0);
    DriveTrain.resetAHRS();
    rocketPath = new RoboPath();
  }

  @Override
  public void autonomousInit() {

  }


  @Override
  public void autonomousPeriodic() {
    TeleOp.run();
  }
  
  @Override
	public void teleopInit(){
    //Starts Teleop 
    
	}

  @Override
  public void teleopPeriodic() {
    TeleOp.run();
    System.out.println(DriveTrain.getLeftEncPos());

  }

  @Override
  public void disabledPeriodic(){
    LEDs.setNeutral();
    
    //DriveTrain.pidDisable();
    //Limelight.dumbLineup();
    Limelight.changePipelineBottom(0);
    Limelight.changePipelineTop(0);
    TeleOp.done();
    //right.length();
    //int lengthRight = right.length();
    
  }


  public void testInit(){
        
    DriveTrain.resetAHRS();
    rocketPath.generateRocketTraj();

  }

  public void testPeriodic(){
    // right1.calculate(DriveTrain.getRightEncPos());
    double[] powers = rocketPath.getPower();

    DriveTrain.drive(powers[0], powers[1]);

  }

  
}
