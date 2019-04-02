package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.jshell.Diag;

public class TeleOp {
	private static XBoxController manip;
	private static XBoxController driver;
	private static TeleOp instance;
	private static double[] rocketSetpoints = {0, -26.12649917602539, -53.45470428466797};
	private static double cargoSetpoints = -17.675899505615234;
	private static double humanPlayerCargo = -15;

	private static int currentSetpoint = 0;
	private static Timer clock;

	private static boolean wasStartPressed = false;
	private static boolean wasBumperPressed = false;

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
		//LEDs.setNeutral();

		Thread policeModeThread = new Thread(() -> {
			if(driver.getStartButton()){
				LEDs.setBlue();
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				LEDs.setRed();
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		});

		policeModeThread.start();
		
	}

	public static void run(){
		/**
		 * ============================
		 *    DRIVER CONTROLS BELOW
		 * ============================
		 */
		//long startTime = System.currentTimeMillis();

		Limelight.changePipelineTop(0);
		//System.out.println("HAS VALID TARGETS: " + Limelight.hasValidTargets());

		if(driver.getRightBumper()){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
		}
		
		if(driver.getLeftBumper()){
			
			if(Elevator.getPosition() > 5 && Elevator.getPosition() < 35){
				Limelight.changePipelineBottom(1);
				//Limelight.driverVisionBottom(0);
				if(Limelight.bottomHasValidTargets()){
					//Limelight.drive();
	
	
					DriveTrain.setAllBrake();
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
	
					
					TeleOp.wasBumperPressed = true;
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
	
	
					DriveTrain.setAllBrake();
					Limelight.dumbLineupTop();

					DriveTrain.arcadeDrive(
						Limelight.output, 
						Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
					);	

					TeleOp.wasBumperPressed = true;
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

		if(manip.getLeftStickYAxis() < -0.15){
			Ingestor.beltUp();
			Ingestor.ingestCargo(1);
			Elevator.setFrontHolderPower(1);
			Elevator.setBackHolderPower(1);
		}else if(manip.getLeftStickYAxis() > 0.1){
			Elevator.setFrontHolderPower(1);
			Elevator.setBackHolderPower(-1);
		}else{
			Ingestor.beltStop();
			Ingestor.ingestCargo(0.0);
			Elevator.setFrontHolderPower(0.0);
			Elevator.setBackHolderPower(0.0);
		}
	
		if(manip.getRightBumper()){
			Elevator.setClawIn();
			LEDs.setLime();
			driver.setRightRumble(1);
			driver.setLeftRumble(1);
		}else{
			Elevator.setClawOut();
			LEDs.setNeutral();
			driver.setRightRumble(0.0);
			driver.setLeftRumble(0.0);
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
				Ingestor.ingestCargo(-manip.getRightStickYAxis());
			}
		}



		if(manip.getAButton()){
			Elevator.zeroElevator();
		}

		if(manip.getBButton()){
			//Elevator.setPosition(30);
			Ingestor.beltDown();

		}else{
			if(manip.getLeftBumper()){
				Elevator.setPower(-0.05, 0);
			}else{
				Elevator.setPower(Utils.expoDeadzone(-manip.getRightStickYAxis(), 0.1, 1.2));
			}
		}

		if(manip.getXButton()){
			//Ingestor.beltDown();
			Elevator.setPosition(TeleOp.humanPlayerCargo);
		}

		if(manip.getBButton()){
			Ingestor.beltDown();
		}
		// CargoShip
		if(manip.getYButton()){
			Elevator.setPosition(TeleOp.cargoSetpoints);
		}

		//Rocket Low
		if(manip.getDPad() == 180){
			Elevator.setPosition(TeleOp.rocketSetpoints[0]);
		}

		//Rocket Middle
		if(manip.getDPad() == 90){
			Elevator.setPosition(TeleOp.rocketSetpoints[1]);
		}

		//Rocket High
		if(manip.getDPad() == 0){
			Elevator.setPosition(TeleOp.rocketSetpoints[2]);
		}
		//System.out.println(Elevator.getPosition());
		// if(manip.getPOV(0) != -1){
		// 	//current % (setpoints length) returns index to next array
		// 	Elevator.setPosition(rocketSetpoints[((currentSetpoint++) % rocketSetpoints.length)]);
		// }
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
		//System.out.println("loop time: " + (System.currentTimeMillis() - startTime));
		
		// if(Elevator.getElevatorLimitSwitch()){
		// 	Elevator.zeroElevator();
		// }

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

	public static void done(){
		driver.setRightRumble(0.0);
		driver.setLeftRumble(0.0);
	}
}
