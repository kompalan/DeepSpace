package frc.robot;

public class TeleOp {
	private static XBoxController driver, manip;
	private static TeleOp instance;
		
	public static TeleOp getInstance() {
		if (instance == null)
			instance = new TeleOp();
		return instance;
	}
	
	private TeleOp(){
		driver = new XBoxController(Constants.XBOX_DRIVER);
		manip = new XBoxController(Constants.XBOX_MANIP);
	}
<<<<<<< HEAD
=======
	
	public static void init(){
		
>>>>>>> 1caf19216e2363db2fab7cacac1f1d0b22065650

	public static void init(){
		// Diagnostics.pushDriveTrainDiagnostics();
		// Diagnostics.pushElevatorDiagnostics();
		// Diagnostics.pushIngestorDiagnostics();
		
	}

	public static void run(){
		//Controller Mappings Here (see picture)
		DriveTrain.arcadeDrive(
			Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, 2), 
			Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 1.2)
		);

		if(DriveTrain.getAvgVelocity() > 50000d){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
		}

		if(driver.getRightBumper()){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
		}

		if(driver.getLeftBumper()){
			Limelight.dock();
		}else{
			DriveTrain.pidDisable();
		}
		
		if(manip.getLeftStickYAxis() < -0.1){
			Ingestor.beltUp();
			Ingestor.ingestCargo(-manip.getLeftStickYAxis());
			Elevator.setFrontHolderForward(1);
			Elevator.setBackHolderForward(1);
		}else if(manip.getLeftStickYAxis() > 0.1){
			Elevator.setFrontHolderForward(1);
			Elevator.setBackHolderForward(-1);
		}else{
			Ingestor.beltStop();
			Ingestor.ingestCargo(0.0);
			Elevator.setFrontHolderForward(0.0);
			Elevator.setBackHolderForward(0.0);
		}
	
		Elevator.setPower(Utils.expoDeadzone(manip.getRightStickYAxis(), 0.1, 1.2));
		
	
		if(manip.getRightBumper()){
			Elevator.setClawIn();
		}else{
			Elevator.setClawOut();
		}
	
		if(manip.getRightTriggerButton()){
			if(Elevator.getFlipped()){
				Elevator.flipClawDown();
			}else{
				Elevator.flipClawUp();
			}
		}
	
		if(manip.getLeftBumper()){
			Ingestor.ingestorDown();
		}else{
			Ingestor.ingestorUp();
		}
		
		if(manip.getLeftBumper() && (manip.getRightStickYAxis() > 0.1 || manip.getRightStickYAxis() < -0.1)){
			Ingestor.beltUp();
			if(Math.abs(manip.getRightStickYAxis()) > 0.1){
				Ingestor.ingestCargo(manip.getRightStickYAxis());
			}
		}

		if(Limelight.hasValidTargets()){
			if(driver.getRightBumper()){
				Limelight.lineUp();
				if(Limelight.getX() == 0){
					driver.setLeftRumble(0.0);
					driver.setRightRumble(0.0);
				}
			}else if(driver.getLeftBumper()){
				Limelight.dock();
			}else{
				DriveTrain.pidDisable();
				DriveTrain.arcadeDrive(-driver.getRightStickYAxis(), driver.getLeftStickXAxis());
			}
			
		}else{
			DriveTrain.pidDisable();
			Limelight.changePipeline(3);

			if(driver.getLeftBumper()){
				DriveTrain.drive(-.3,-.3);
			}
		}
		Diagnostics.pushElevatorDiagnostics();
		Diagnostics.pushIngestorDiagnostics();
	}
}
