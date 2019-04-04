/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;


public class Robot extends TimedRobot{
  private static final String ROBOT_FILE_PATH = "/home/lvuser/config/config.json";
  Trajectory rocket;
  public static double wheelbase_width = 0.7239; //meters
  public static double distance = 5.334; //meters
  public static double wheel_diameter = 0.1524; //meters
  //public TankModifier modifier;
  double angleDiff = 0.0;

  TankModifier modifier;
  Trajectory left, right;
  EncoderFollower right1, left1, rocket1;

  public TestPath testPath;

  private Notifier pathNotifier;
  //SendableChooser<Integer> autochooser;

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
    testPath = new TestPath();


    // this.setupFollower();
    try{
      testPath.setup();
      System.out.println("Path Setup Complete!");
    }catch(IOException ioe){
      ioe.printStackTrace();
    }
        
    pathNotifier = new Notifier(testPath);
  }

  @Override
  public void autonomousInit() {
    //rocketPath = new RoboPath(autochooser.getSelected());



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
    System.out.println(DriveTrain.getAHRS());

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
    
    DriveTrain.setAllBreak();
    
    DriveTrain.resetEncs();
    System.out.println("INITIAL HYRO POSITION: " + DriveTrain.getAHRS());
    pathNotifier.startPeriodic(0.02);


  }

  public void testPeriodic(){

  }


  public class TestPath implements Runnable{

    Pathfinder instance;
    Trajectory rocket;
    Trajectory left, right;
    double wheelbase_width = 0.7239; //meters
    double distance = 5.334; //meters
    double wheel_diameter = 0.1524; //meters
    double angleDiff = 0.0;

    TankModifier modifier;
    EncoderFollower right1, left1;
    

    public void setup() throws IOException{
        left = PathfinderFRC.getTrajectory("Test2.right");
        right = PathfinderFRC.getTrajectory("Test2.left");
        
        //rocket1 = new EncoderFollower(rocket);
        left1 = new EncoderFollower(left);
        right1 = new EncoderFollower(right);
    
        //left1.reset();
        left1.configureEncoder(DriveTrain.getLeftEncPos(), 36, wheel_diameter);
        left1.configurePIDVA(0.01, 0.0, 0.0, 1d / 15d, 0);
    
        right1.configureEncoder(DriveTrain.getRightEncPos(), 36, wheel_diameter);
        right1.configurePIDVA(0.01, 0.0, 0.0, 1d / 15d, 0);
        
    }

    @Override
    public void run(){
        if (left1.isFinished() && right1.isFinished()) {
            
            DriveTrain.drive(0d, 0d);
            pathNotifier.stop();

        } else {
            DriveTrain.resetAHRS();
            double left_speed = left1.calculate(DriveTrain.getLeftEncPos());
            double right_speed = right1.calculate(DriveTrain.getRightEncPos());
            double heading = DriveTrain.getAHRS();
            double desired_heading = Pathfinder.r2d(left1.getHeading());
            double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
      
            
      
            double turn =  0.05 * (-1.0/80.0) * heading_difference;
            System.out.println("LEFT: " +  (left_speed + turn));
            System.out.println("RIGHT: " + (right_speed - turn));
            System.out.println("HYRO: " + DriveTrain.getAHRS());

            //https://www.chiefdelphi.com/t/problems-with-pathfinder-motion-profiling/163830/3
            DriveTrain.drive((left_speed + turn), 
                            (right_speed - turn));
        }
    }
  }


}

