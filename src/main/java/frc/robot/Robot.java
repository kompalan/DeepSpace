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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot{
  private static final String ROBOT_FILE_PATH = "/home/lvuser/config/config.json";

  public RoboPath rocketPath;
  private Notifier pathNotifier;

  @Override
  public void robotInit() {
    // JSONConstants.setFilePath(this.ROBOT_FILE_PATH);
    // JSONConstants.populateMap();
    // JSONConstants.test();

    DriveTrain.getInstance();
    TeleOp.getInstance();
    Elevator.getInstance();
    Ingestor.getInstance();
    Diagnostics.getInstance();
    LEDs.getInstance();
    System.out.println("Robot Code Started");

    rocketPath = new RoboPath();
    pathNotifier = new Notifier(rocketPath);

    // this.setupFollower();
    try{
      rocketPath.setup();
    }catch(IOException ioe){
      ioe.printStackTrace();
    }
    
  }

  @Override
  public void autonomousInit() {
    TeleOp.init();
  }


  @Override
  public void autonomousPeriodic() {
    TeleOp.run();
  }
  
  @Override
	public void teleopInit(){
    //Starts Teleop 
    TeleOp.init();
	}

  @Override
  public void teleopPeriodic() {
    TeleOp.run();
  }

  @Override
  public void disabledPeriodic(){
    //LEDs.setNeutral();
    TeleOp.done();
  }

  @Override
  public void testInit(){
        
    DriveTrain.resetAHRS();
    DriveTrain.setAllBrake();
    DriveTrain.resetEncs();

    pathNotifier.startPeriodic(0.02);
  }

  public void testPeriodic(){
    if(RoboPath.isDone){
      pathNotifier.stop();
    }
  }
}
