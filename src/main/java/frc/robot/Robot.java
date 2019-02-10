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
  
  private static XBoxController manip, driver;
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
    manip = new XBoxController(1);
    driver = new XBoxController(0);
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
    //TeleOp.init();
	}

  @Override
  public void teleopPeriodic() {
    //TeleOp.run();
    Diagnostics.pushDriveTrainDiagnostics();
    Diagnostics.pushElevatorDiagnostics();
    Diagnostics.pushIngestorDiagnostics();
    Ingestor.beltStop();
    Ingestor.ingestCargo(0.0);
    Elevator.setFrontHolderForward(0.0);
    Elevator.setBackHolderForward(0.0);
    if(manip.getLeftStickYAxis() < -0.1){
      Ingestor.beltUp();
      Ingestor.ingestCargo(-manip.getLeftStickYAxis());
      Elevator.setFrontHolderForward(1);
      Elevator.setBackHolderForward(1);
    }else if(manip.getLeftStickYAxis() > 0.1){
      Elevator.setFrontHolderForward(1);
      Elevator.setBackHolderForward(-1);
    }

    if(Math.abs(manip.getRightStickYAxis()) > 0.1){
      Elevator.setPower(Utils.expoDeadzone(manip.getRightStickYAxis(), 0.1, 1.2));
    }

    if(manip.getRightBumper()){
      Elevator.setClawIn();
    }else{
      Elevator.setClawOut();
    }

    if(manip.getRightTriggerButton()){
      Elevator.flipClawUp();
    }else{
      Elevator.flipClawDown();
    }

    if(manip.getLeftBumper()){
      Ingestor.ingestorDown();
    }else{
      Ingestor.ingestorUp();
    }

		DriveTrain.arcadeDrive(
      Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.15, 2), 
      Utils.expoDeadzone(driver.getRightStickYAxis(), 0.15, 1.2)
      );
    
    System.out.println("Left Stick: " + driver.getLeftStickXAxis());
    System.out.println("Right Stick: " + driver.getRightStickYAxis());
    
    if(driver.getRightBumper()){
      DriveTrain.shiftUp();
    }else{
      DriveTrain.shiftDown();
    }

  }

  @Override
  public void disabledPeriodic(){
    Diagnostics.pushDriveTrainDiagnostics();
    Diagnostics.pushElevatorDiagnostics();
    Diagnostics.pushIngestorDiagnostics();
  }
}
