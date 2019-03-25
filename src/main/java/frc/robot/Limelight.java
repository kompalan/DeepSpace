package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

public class Limelight {
	/**
	 * <summary>
	 * Since Limelight Reads From Network Tables,
	 * this file is simply networktables operations, like Reading and Writing
	 * 
	 * This file also contains Limelight Based PID for Turning to Vision Target Angles
	 * </summary>
	 */

	public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	public static DriverStation ds = DriverStation.getInstance();
	public static double error, integral, derivative, previous_error, output;

	
	public static void testFeed(){
		double x = table.getEntry("tx").getDouble(0.0);
		double y = table.getEntry("ty").getDouble(0.0);		
	}
	
	public static double getX(){
		return table.getEntry("tx").getDouble(0.0);
	}
	
	public static double getY(){
		return table.getEntry("ty").getDouble(0.0);
	}
	
	public static double getA(){
		return table.getEntry("ta").getDouble(0.0);
	}
	
	public static boolean hasValidTargets(){
		if(table.getEntry("tv").getDouble(0.0) == 1){
			return true;
		}
		return false;
	}
	
	public static void changePipeline(int pipeline_num){
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		pipeline.setValue(pipeline_num);
	}
	
	//0 enables vision processing to target
	//1 disables vision processing and increases exposure to act purely as a camera
	public static void driverVision(int driver_control){
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(driver_control);
	}
	
	public static double getContourArea(){
		return table.getEntry("ta0").getDouble(0.0);
	}
	
	public static double getContourX(){
		return table.getEntry("ty1").getDouble(0.0);
	}

	public static void PID(double setpoint){
		Limelight.error = setpoint - DriveTrain.getAHRS();
		Limelight.integral += (error*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
		
        derivative = (error - Limelight.previous_error) / .02;

		Limelight.previous_error = Limelight.error;
	
		Limelight.output = Constants.LIMELIGHT_P*error + Constants.LIMELIGHT_I*Limelight.integral + Constants.LIMELIGHT_D*Limelight.derivative;
	}

	public static void dumbLineup(){
		Limelight.testFeed();
		//double x = Math.abs(Limelight.getX()) - 1;

		Limelight.PID(DriveTrain.getAHRS() + Limelight.getX());

		//DriveTrain.arcadeDrive(Limelight.output, 0);
		//DriveTrain.drive(Limelight.output, Limelight.output);
	}
	public static double getPipeline(){
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		return pipeline.getDouble(-1);
	}
	

	public static void dock(){
		double distance = Utils.distFrom(Utils.degToRad(Limelight.getX()),Utils.degToRad(Limelight.getY()));

		Limelight.dumbLineup();
		DriveTrain.arcadeDrive(0.3, 0);
		//Limelight.lineUp();
		if(distance >= 50){
			Limelight.changePipeline(1);
		}

		if(distance <=20){
			Limelight.changePipeline(1);
			DriveTrain.drive(Constants.LINEUP_FULL_SPEED, Constants.LINEUP_HALF_SPEED * Constants.DRIVE_STRAIGHT_CONSTANT);
		}else{
			DriveTrain.drive(Constants.LINEUP_FULL_SPEED, Constants.LINEUP_FULL_SPEED * Constants.DRIVE_STRAIGHT_CONSTANT);
		}
	}

	public static void drive(){
		DriveTrain.arcadeDrive(0.1, 0);
	}
}
