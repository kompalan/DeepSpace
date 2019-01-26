package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		driver = new XBoxController((int) JSONConstants.get("xbPosDriver"));
		manip = new XBoxController((int) JSONConstants.get("xbPosManip"));
	}
	
	public static void init(){
		//Should init DriveTrain, Elevator etc.
	}
	
	public static void run(){

		//Controller Mappings Here (see picture)
		DriveTrain.arcadeDrive(driver.getLeftStickXAxis(), Utils.negPowTwo(driver.getRightStickYAxis()));
	
	}
}