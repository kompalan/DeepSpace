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
		//Should get Instance of  DriveTrain, Elevator, and other stuff
		driver = new XBoxController(Constants.XBOX_DRIVER);
		manip = new XBoxController(Constants.XBOX_MANIP);
	}
	
	public static void init(){
		//Should init DriveTrain, Elevator etc.

	}
	
	public static void run(){

		//Controller Mappings Here (see picture)
		

		if(driver.getLeftStickXAxis() != 0 || driver.getRightStickYAxis() != 0){
			DriveTrain.arcadeDrive(driver.getLeftStickXAxis(), Utils.negPowTwo(driver.getRightStickYAxis()));
		}

		// if(driver.getRightBumper()){
		// 	DriveTrain.shiftUp();
		// }else{
		// 	DriveTrain.shiftDown();
		// }

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

		if(manip.getRightBumper()){
			Elevator.setClawIn();
		}else{
			Elevator.setClawOut();
		}

		if(manip.getYButton()){
			//Flip Up?
		}else{
			//Flip Down?
		}

		if(manip.getRightTriggerButton()){
			
		}
	}
}