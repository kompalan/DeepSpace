package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;

public class TeleOp {
	private static XBoxController manip;
	private static XBoxController driver;
	private static TeleOp instance;
	private static boolean wasStartPressed = false;
	private static boolean wasBumperPressed = false;
	


	private static double[] rocketSetpoints = {-3, 26.6, 55}; //Needs to be changed every match
	private static double cargoSetpoint = 13.5; //Same with this one
	private static double humanPlayerCargo = 15;

	public static TeleOp getInstance() {
		if (instance == null)
			instance = new TeleOp();
		return instance;
	}
	
	private TeleOp(){
		//Driver Should be Set to Port 0, while Manip should be set to Port 1
		//Port Numbers Correspond to Ports on DS
		driver = new XBoxController(Constants.XBOX_DRIVER);
		manip = new XBoxController(Constants.XBOX_MANIP);
	}
		

	public static void init(){

		LEDs.setNeutral();
		Elevator.zeroElevator();
		
	}

	public static void run(){
		/**
		 * ============================
		 *    DRIVER CONTROLS BELOW
		 * ============================
		 */

			
		/**
		 * Simple Shifter: If we get the right bumper, shift to high gear
		 * Default: Low Gear 
		 */
		if(driver.getRightBumper()){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
		}
		
		/**
		 * If we get Left Bumper, change the pipeline to the "vision target pipeline" (1)
		 * along with "driver vision" to enable targeting (0) and stop acting purely as a camera
		 * Then Check if we have valid targets to line up to
		 * Then lineup ONCE and drive straight
		 * 
		 * This is built on the assumption that driver is mostly lined up with target and 
		 * simply needs a little help.
		 */
		if(driver.getLeftBumper()){
			
			if(Elevator.getPosition() > 5 && Elevator.getPosition() < 35){
				Limelight.changePipelineBottom(1);
				//Limelight.driverVisionBottom(0);
				if(Limelight.bottomHasValidTargets()){
					//Limelight.drive();
	
	
					DriveTrain.setAllBreak();
					Limelight.dumbLineupBottom();
	
					if(driver.getRightStickYAxis() > 0.1){
						DriveTrain.arcadeDrive(
							Limelight.output, 
							Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
						);
					}else if(driver.getRightStickYAxis() < -0.1){
						DriveTrain.arcadeDrive(
							Limelight.output, 
							Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
						);
					}
	
					
					wasBumperPressed = true;
				}else{
					DriveTrain.arcadeDrive(
					Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, Constants.TURN_EXPO_CONSTANT),
					Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, Constants.DRIVE_EXPO_CONSTANT));
				};
				
			}else{
				Limelight.changePipelineTop(1);
				//Limelight.driverVisionTop(0);
				if(Limelight.topHasValidTargets()){
					//Limelight.drive();
	
	
					DriveTrain.setAllBreak();
					Limelight.dumbLineupTop();

					DriveTrain.arcadeDrive(
						Limelight.output, 
						Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
					);	

					wasBumperPressed = true;
				}else{
					DriveTrain.arcadeDrive(
					Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, Constants.TURN_EXPO_CONSTANT),
					Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, Constants.DRIVE_EXPO_CONSTANT));
				};
			}
			
		}else{

			//Normal Driver Pipeline along with "driver vision" in order to see the Field During Sandstorm
			TeleOp.wasBumperPressed = false;
			DriveTrain.setAllCoast();
			Limelight.changePipelineTop(0);
			Limelight.changePipelineBottom(0);
			//Limelight.driverVisionTop(1);
			//Limelight.driverVisionBottom(0);
				if(driver.getRightTriggerAxis()>0.1){
					DriveTrain.arcadeDrive(
					Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, Constants.TURN_EXPO_CONSTANT)*0.3,
					Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, Constants.DRIVE_EXPO_CONSTANT)*0.3
				);
			}else{
				DriveTrain.arcadeDrive(
				Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, Constants.TURN_EXPO_CONSTANT),
				Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, Constants.DRIVE_EXPO_CONSTANT)
				);
			}
		}

	
		/**
		 * ============================
		 *     MANIP CONTROLS BELOW
		 * ============================
		 */

		/**
		 * If we get manip pulling back on the joystick, then we know that he/she wants to ingest, 
		 * meaning that belts need to go up, rollerbar needs to set power to INTAKE, and front holders
		 * and back holders need to be moving the opposite directions
		 */
		
		if(manip.getLeftStickYAxis() < -0.15){
			Ingestor.beltUp();
			Ingestor.ingestCargo(1);
			Elevator.setFrontHolderPower(1); //Front holder moves backward with "1" power
			Elevator.setBackHolderPower(1); //Back holder always moves forward
		}else if(manip.getLeftStickYAxis() > 0.1){
			//If manip pushes joystick forward, both cargo holders need to move forward
			Elevator.setFrontHolderPower(1); //Front holder moveds forward with "-1" power
			Elevator.setBackHolderPower(-1); //Back holder always moves forward
		}else{
			//Belts, Rollerbar and Cargo Holders do not need to move when this joystick is not activated
			Ingestor.beltStop();
			Ingestor.ingestCargo(0.0);
			Elevator.setFrontHolderPower(0.0);
			Elevator.setBackHolderPower(0.0);
		}

		//If Right Bumper is Pressed Do the Crab thing
		if(manip.getRightBumper()){
			Elevator.setClawIn();
			LEDs.setLime();
			driver.setLeftRumble(1.0);
			driver.setRightRumble(1.0);
		}else{
			//Set Default to Out (Does not need to be latching)
			Elevator.setClawOut();
			driver.setLeftRumble(0);
			driver.setRightRumble(0);
		}

		//Latching Trigger to Flipping Up Elevator Claw Thing

		/**
		 * Check for a time when Trigger is pressed but hasn't been pressed in a previous loop in order
		 * to press the trigger with out it spazzing out
		*/ 
		boolean isStartPressed = manip.getRightTriggerButton();
		if(isStartPressed && !wasStartPressed){
			if(Elevator.getFlipped()){
				Elevator.flipClawDown();
			}else{
				Elevator.flipClawUp();
			}
			
		}
		wasStartPressed = isStartPressed;
	
		//Put Ingestor Down In Order to Grab Cargo
		if(manip.getLeftBumper()){
			Ingestor.ingestorDown();
			LEDs.setViolet();
		}else{
			Ingestor.ingestorUp();
		}
		
		//Dont know why this exists...
		//TODO: Make sure this is important... if not remove and test
		if(manip.getLeftBumper() && (manip.getRightStickYAxis() > 0.1 || manip.getRightStickYAxis() < -0.1)){
			Ingestor.beltUp();
			if(Math.abs(manip.getRightStickYAxis()) > 0.15){
				Ingestor.ingestCargo(manip.getRightStickYAxis());
			}
		}


		if(manip.getBButton()){
			
			Ingestor.beltDown();
		}



		if(manip.getAButton()){
			//Manual Workaround due to absence of limit switch
			Elevator.zeroElevator();
		}

		if(manip.getXButton()){
			//If we get two balls in the cargo transport system
			Elevator.setPosition(humanPlayerCargo);

		}else{
			/**
			 * If we are ingesting cargo, we need to set the elevator 
			 * to a negative power to counteract the arb feed forward
			 */
			if(manip.getLeftBumper()){
				Elevator.setPower(-0.05, 0);
			}else{
				Elevator.setPower(Utils.expoDeadzone(manip.getRightStickYAxis(), 0.1, 1.2));
			}
		}

		/**
		 * SEE THE ARRAY AT THE TOP OF FILE
		 * NEED TO CHANGE THIS BEFORE EVERY MATCH OR
		 * SETPOINTS WILL BE OFF
		 */
		if(manip.getYButton()){
			//Cargo Setpoint Position
			Elevator.setPosition(cargoSetpoint);
		}

		
		if(manip.getPOV() == 180){
			//Elevator setpoint for the bottom of the rocket
			Elevator.setPosition(rocketSetpoints[0]);
		}

		if(manip.getPOV() == 90){
			//Elevator setpoint for the middle of the rocket
			Elevator.setPosition(rocketSetpoints[1]);
		}

		if(manip.getPOV() == 0){
			//Elevator setpoint for the bottom of the rocket
			Elevator.setPosition(rocketSetpoints[2]);
		}

		if(Elevator.isLimitSwitchActive()){
			Elevator.zeroElevator();
			
		}

		if(manip.getRightBumper()){
			LEDs.setLime();
			if(Elevator.getPosition() > 5 && Elevator.getPosition() < 35){
				NetworkTableInstance.getDefault().getTable("limelight-top").getEntry("ledMode").setNumber(2);
			}else{
				NetworkTableInstance.getDefault().getTable("limelight-bottom").getEntry("ledMode").setNumber(2);
			}
		}else if(manip.getLeftBumper()){
			LEDs.setViolet();
		}else if(driver.getLeftBumper() && (Limelight.topHasValidTargets() | Limelight.bottomHasValidTargets() ) ){
			LEDs.setLava();
		}else if(driver.getLeftBumper() && (!Limelight.topHasValidTargets() & !Limelight.bottomHasValidTargets() ) ){
			LEDs.setWhiteStrobe();

		}else{
			LEDs.setNeutral();
			NetworkTableInstance.getDefault().getTable("limelight-bottom").getEntry("ledMode").setNumber(0);
			NetworkTableInstance.getDefault().getTable("limelight-top").getEntry("ledMode").setNumber(0);
		}
		
	}

	public static void done(){
		driver.setLeftRumble(0.0);
		driver.setRightRumble(0.0);
	}

}
