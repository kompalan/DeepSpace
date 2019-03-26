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

	public static DriverStation ds = DriverStation.getInstance();
	public static double error, integral, derivative, previous_error, output;

	public static String networkTableName = "limelight-top";

	public static NetworkTable topTable = NetworkTableInstance.getDefault().getTable("limelight-top");
	public static NetworkTable bottomTable = NetworkTableInstance.getDefault().getTable("limelight-bottom");
	
	public static void testFeedTop(){
		double x = topTable.getEntry("tx").getDouble(0.0);
		double y = topTable.getEntry("ty").getDouble(0.0);		
	}
	

	public static void testFeedBottom(){
		double x = bottomTable.getEntry("tx").getDouble(0.0);
		double y = bottomTable.getEntry("ty").getDouble(0.0);		
	}
	

	public static double getXTop(){
		return topTable.getEntry("tx").getDouble(0.0);
	}

	public static double getXBottom(){
		return bottomTable.getEntry("tx").getDouble(0.0);
	}
	
	public static double getY(){
		return topTable.getEntry("ty").getDouble(0.0);
	}
	
	public static double getA(){
		return topTable.getEntry("ta").getDouble(0.0);
	}
	
	public static boolean hasValidTargets(){
		if(topTable.getEntry("tv").getDouble(0.0) == 1){
			return true;
		}
		return false;
	}

	public static void changeNetworkTablesEntry(String networkTableEntry){
		Limelight.networkTableName = networkTableEntry;
	}
	
	public static void changePipelineTop(int pipeline_num){
		NetworkTableEntry pipeline = topTable.getEntry("pipeline");
		pipeline.setValue(pipeline_num);
	}

	public static void changePipelineBottom(int pipeline_num){
		NetworkTableEntry pipeline = bottomTable.getEntry("pipeline");
		pipeline.setValue(pipeline_num);
	}
	
	//0 enables vision processing to target
	//1 disables vision processing and increases exposure to act purely as a camera
	public static void driverVision(int driver_control){
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(driver_control);
	}
	
	public static double getContourArea(){
		return topTable.getEntry("ta0").getDouble(0.0);
	}
	
	public static double getContourX(){
		return topTable.getEntry("ty1").getDouble(0.0);
	}

	public static void PID(double setpoint){
		Limelight.error = setpoint - DriveTrain.getAHRS();
		Limelight.integral += (error*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
		
        derivative = (error - Limelight.previous_error) / .02;

		Limelight.previous_error = Limelight.error;
	
		Limelight.output = Constants.LIMELIGHT_P*error + Constants.LIMELIGHT_I*Limelight.integral + Constants.LIMELIGHT_D*Limelight.derivative;
	}

	public static void dumbLineupTop(){
		Limelight.testFeedTop();
		//double x = Math.abs(Limelight.getX()) - 1;

		Limelight.PID(DriveTrain.getAHRS() + Limelight.getXTop());

		//DriveTrain.arcadeDrive(Limelight.output, 0);
		//DriveTrain.drive(Limelight.output, Limelight.output);
	}

	public static void dumbLineupBottom(){
		Limelight.testFeedBottom();
		//double x = Math.abs(Limelight.getX()) - 1;

		Limelight.PID(DriveTrain.getAHRS() + Limelight.getXBottom());

		//DriveTrain.arcadeDrive(Limelight.output, 0);
		//DriveTrain.drive(Limelight.output, Limelight.output);
	}

	public static double getPipeline(){
		NetworkTableEntry pipeline = topTable.getEntry("pipeline");
		return pipeline.getDouble(-1);
	}

	public static void drive(){
		DriveTrain.arcadeDrive(0.1, 0);
	}
}
