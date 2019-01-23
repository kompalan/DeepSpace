/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.XBoxController;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot{
  
  private static XBoxController driver;

  @Override
  public void robotInit() {
    DriveTrain.getInstance();
    TeleOp.getInstance();
  }

  @Override
  public void robotPeriodic() {
  }


  @Override
  public void autonomousInit() {

  }


  @Override
  public void autonomousPeriodic() {

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
  public void testPeriodic() {
   
  }
}
