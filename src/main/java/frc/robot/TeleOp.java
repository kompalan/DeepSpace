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
	
	public static void init(){
		

	}
	
	public static void run(){

		//Controller Mappings Here (see picture)
		Diagnostics.pushDriveTrainDiagnostics();
		Diagnostics.pushElevatorDiagnostics();
		Diagnostics.pushIngestorDiagnostics();

		DriveTrain.arcadeDrive(
			Utils.expoDeadzone(driver.getLeftStickXAxis(), 0.1, 1.2), 
			Utils.expoDeadzone(driver.getRightStickYAxis(), 0.1, 2)
		);

		if(driver.getRightBumper()){
			DriveTrain.shiftUp();
		}else{
			DriveTrain.shiftDown();
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
		
		//System.out.printf("Left: %05f  Right: %05f\n" , driver.getLeftStickXAxis(),driver.getRightStickYAxis());    
		if(manip.getLeftBumper() && (manip.getRightStickYAxis() > 0.1 || manip.getRightStickYAxis() < -0.1)){
			Ingestor.beltUp();
			if(Math.abs(manip.getRightStickYAxis()) > 0.1){
				Ingestor.ingestCargo(manip.getRightStickYAxis());
			}
		}else{
			if(manip.getRightStickYAxis() > 0.1 || manip.getRightStickYAxis() < -0.1){
				Elevator.setPower(-manip.getRightStickYAxis());
			}
		}
	}
}
