package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleOp {
	private static XBoxController driver;
	private static TeleOp instance;
		
	public static TeleOp getInstance() {
		if (instance == null)
			instance = new TeleOp();
		return instance;
	}
	
	private TeleOp(){
		//Should Init DriveTrain, Elevator, and other stuff
		driver = new XBoxController(0);
	}
	
	public static void init(){
		
	}
	
	public static void run(){

        //Basically a bunch of if statements
		DriveTrain.arcadeDrive(driver.getRightStickYAxis(), Utils.negPowTwo(driver.getLeftStickXAxis()));
	
	}
}