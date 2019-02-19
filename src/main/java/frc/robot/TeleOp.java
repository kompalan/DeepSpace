package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleOp {
	private static XBoxController manip;
	private static XBoxController driver;
	private static TeleOp instance;
	private static boolean wasStartPressed = false;
	private static boolean wasDPadUpPressed = false;

	private static double[] rocketSetpoints = {0, 31.13533592224121, 55};
	//15.026803970336914
	private static double cargoSetpoint = 15.026803970336914;
	private static int currentSetpointIndex = 0;

	public static TeleOp getInstance() {
		if (instance == null)
			instance = new TeleOp();
		return instance;
	}
	
	private TeleOp(){
		driver = new XBoxController(Constants.XBOX_DRIVER);
		manip = new XBoxController(Constants.XBOX_MANIP);
	}
		

	public static void init(){
		// SmartDashboard.putNumber("P Gain", Constants.ELEVATOR_kP);
		// SmartDashboard.putNumber("I Gain", Constants.ELEVATOR_kI);
		// SmartDashboard.putNumber("D Gain", Constants.ELEVATOR_kD);
		// SmartDashboard.putNumber("I Zone", Constants.ELEVATOR_kIZ);
		// SmartDashboard.putNumber("Feed Forward", Constants.ELEVATOR_kFF);
		// SmartDashboard.putNumber("Max Output", Constants.ELEVATOR_MAX_OUTPUT);
		// SmartDashboard.putNumber("Min Output", Constants.ELEVATOR_MIN_OUTPUT);

		// SmartDashboard.putNumber("Max Velocity", Constants.ELEVATOR_MAX_VEL);
		// SmartDashboard.putNumber("Min Velocity", Constants.ELEVATOR_MIN_VEL);
		// SmartDashboard.putNumber("Max Acceleration", Constants.ELEVATOR_MAX_ACC);
		// SmartDashboard.putNumber("Allowed Loop Error", Constants.ELEVATOR_ALLOWED_ERR);

		// Thread thread1 = new Thread(() -> {
		// 	while(!Thread.interrupted()){
		// 		//Push Diagnostics to Shuffleboard 
		// 		//Diagnostics.pushElevatorDiagnostics();
		// 		Diagnostics.pushErrorDiagnostics();

		// 		try{
		// 			Thread.sleep(100);
		// 		}catch(InterruptedException ie){
		// 			ie.printStackTrace();

		// 			return;
		// 		}
		// 	}
		// });

		// thread1.setPriority(1);
		// thread1.start();

		LEDs.setNeutral();
		
	}

	public static void run(){
		/**
		 * ============================
		 *    DRIVER CONTROLS BELOW
		 * ============================
		 */

			

		if(driver.getRightBumper()){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
		}

		if(driver.getXButton()){
			Elevator.setPosition(74);
		}

		if(driver.getAButton()){
			Elevator.setPosition(46.5);
		}

		if(driver.getBButton()){
			Elevator.setPosition(18.5);
		}
		
		if(driver.getLeftBumper()){
			Limelight.changePipeline(1);

			if(Limelight.hasValidTargets()){
				Limelight.dumbLineup();
				DriveTrain.driveStraight(0.1);
				
			}else{
				driver.setLeftRumble(0.0);
				driver.setRightRumble(0.0);
			}
		}else{
			Limelight.changePipeline(0);
			DriveTrain.arcadeDrive(
				Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, 2), 
				Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
			);
		}

	
		/**
		 * ============================
		 *     MANIP CONTROLS BELOW
		 * ============================
		 */

		if(manip.getLeftStickYAxis() < -0.15){
			Ingestor.beltUp();
			Ingestor.ingestCargo(1);
			Elevator.setFrontHolderPower(1);
			Elevator.setBackHolderPower(1);
		}else if(manip.getLeftStickYAxis() > 0.1){
			Elevator.setFrontHolderPower(-1);
			Elevator.setBackHolderPower(1);
		}else{
			Ingestor.beltStop();
			Ingestor.ingestCargo(0.0);
			Elevator.setFrontHolderPower(0.0);
			Elevator.setBackHolderPower(0.0);
		}
	
		if(manip.getRightBumper()){
			Elevator.setClawIn();
			LEDs.setLime();
		}else{
			Elevator.setClawOut();
			
		}

		boolean isStartPressed = manip.getRightTriggerButton();
		if(isStartPressed && !wasStartPressed){
			if(Elevator.getFlipped()){
				Elevator.flipClawDown();
			}else{
				Elevator.flipClawUp();
			}
			
		}
		wasStartPressed = isStartPressed;
	

		if(manip.getLeftBumper()){
			Ingestor.ingestorDown();
		}else{
			Ingestor.ingestorUp();
		}
		
		if(manip.getLeftBumper() && (manip.getRightStickYAxis() > 0.1 || manip.getRightStickYAxis() < -0.1)){
			Ingestor.beltUp();
			if(Math.abs(manip.getRightStickYAxis()) > 0.15){
				Ingestor.ingestCargo(manip.getRightStickYAxis());
			}
		}



		if(manip.getAButton()){
			Elevator.zeroElevator();
		}

		if(manip.getXButton()){
			//Elevator Setpoint Position
			Ingestor.beltDown();
		}else{
			if(manip.getLeftBumper()){
				Elevator.setPower(-0.05, 0);
			}else{
				Elevator.setPower(Utils.expoDeadzone(manip.getRightStickYAxis(), 0.1, 1.2));
			}
		}

		if(manip.getYButton()){
			Elevator.setPosition(cargoSetpoint);
		}

		//Elevator Bottom
		if(manip.getPOV() == 180){
			Elevator.setPosition(rocketSetpoints[0]);
		}

		//Elevator Middle
		if(manip.getPOV() == 90){
			Elevator.setPosition(rocketSetpoints[1]);
		}

		//Elevator Top
		if(manip.getPOV() == 0){
			Elevator.setPosition(rocketSetpoints[2]);
		}




	
		// if(manip.getPOV(1) != -1){
		// 	//current % (setpoints length) returns index to next array
		// 	//Elevator.setPosition(rocketSetpoints[((currentSetpoint++) % rocketSetpoints.length)]);
		// 	Elevator.setPosition(2.0);
		// }

		// else if(manip.getPOV(2) != -1){
		// 	Elevator.setPosition(29.332155227661133);
		// }

		// else if(manip.getPOV(3) != -1){
		// 	Elevator.setPosition(52.0);
		// }

		LEDs.setNeutral();

		/**
		 * ||====================================||
		 * ||====================================||
		 * || TESTING ONLY!! REMOVE BEFORE COMP! ||
		 * ||====================================||
		 * ||====================================||
		 */
		// double p = SmartDashboard.getNumber("P Gain", Constants.ELEVATOR_kP);
		// double i = SmartDashboard.getNumber("I Gain", Constants.ELEVATOR_kI);
		// double d = SmartDashboard.getNumber("D Gain", Constants.ELEVATOR_kD);
		// double iz = SmartDashboard.getNumber("I Zone", Constants.ELEVATOR_kIZ);
		// double ff = SmartDashboard.getNumber("Feed Forward", Constants.ELEVATOR_kFF);
		// double maxOut = SmartDashboard.getNumber("Max Output", Constants.ELEVATOR_MAX_OUTPUT);
		// double minOut = SmartDashboard.getNumber("Min Output", Constants.ELEVATOR_MIN_OUTPUT);

		// double maxVel = SmartDashboard.getNumber("Max Velocity", Constants.ELEVATOR_MAX_VEL);
		// double minVel = SmartDashboard.getNumber("Min Velocity", Constants.ELEVATOR_MIN_VEL);
		// double maxAcc = SmartDashboard.getNumber("Max Acceleration", Constants.ELEVATOR_MAX_ACC);
		// double loopErr = SmartDashboard.getNumber("Allowed Loop Error", Constants.ELEVATOR_ALLOWED_ERR);
		
		
		// if((p != Constants.ELEVATOR_kP)) { Elevator.setELEVATOR_P(p); Constants.ELEVATOR_kP = p; System.out.println("Changed Value to: " + p); }
		// if((i != Constants.ELEVATOR_kI)) { Elevator.setELEVATOR_I(i); Constants.ELEVATOR_kI = i; }
		// if((d != Constants.ELEVATOR_kD)) { Elevator.setELEVATOR_D(d); Constants.ELEVATOR_kD = d; }
		// if((iz != Constants.ELEVATOR_kIZ)) { Elevator.setELEVATOR_IZ(iz); Constants.ELEVATOR_kIZ = iz; }
		// if((ff != Constants.ELEVATOR_kFF)) { Elevator.setELEVATOR_FF(ff);; Constants.ELEVATOR_kFF = ff; }

		// if((maxOut != Constants.ELEVATOR_MAX_OUTPUT) || (minOut != Constants.ELEVATOR_MIN_OUTPUT)) { 
		// 	Elevator.setELEVATOR_KOUTPUT(minOut, maxOut);
		// 	Constants.ELEVATOR_MIN_OUTPUT = minOut;
		// 	Constants.ELEVATOR_MAX_OUTPUT = maxOut;
		// }
		
		// if((maxVel  != Constants.ELEVATOR_MAX_VEL)) { Elevator.setELEVATOR_MAXVEL(maxVel); Constants.ELEVATOR_MAX_VEL = maxVel; }
		// if((minVel  != Constants.ELEVATOR_MIN_VEL)) { Elevator.setELEVATOR_MINVEL(minVel); Constants.ELEVATOR_MIN_VEL = minVel; }
		// if((maxAcc  != Constants.ELEVATOR_MAX_ACC)) { Elevator.setELEVATOR_MAXACC(maxAcc); Constants.ELEVATOR_MAX_ACC = maxAcc; }
		// if((loopErr != Constants.ELEVATOR_ALLOWED_ERR)) { Elevator.setELEVATOR_MAXERR(loopErr); Constants.ELEVATOR_ALLOWED_ERR = loopErr; }

		
		// if(System.currentTimeMillis() - startTime > 10){
		// 	System.out.println(System.currentTimeMillis() - startTime);
		// }

	}
}
