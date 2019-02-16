/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot{
  private static final String ROBOT_FILE_PATH = "/home/lvuser/config/config.json";

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
    LEDs.setNeutral();
  }
}
