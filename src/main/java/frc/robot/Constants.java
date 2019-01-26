package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Constants {
	/**
	 * Use This File Only For Emergencies (like JSONConstants cannot find the config file)
	 */
	//Drive Train
	public static final int DT_TALON_RIGHTFRONT = 20; // has encoder
	public static final int DT_TALON_RIGHTMIDDLE = 1;
	public static final int DT_TALON_RIGHTBACK = 2;

	public static final int DT_TALON_LEFTFRONT = 13;
	public static final int DT_TALON_LEFTMIDDLE = 14;
	public static final int DT_TALON_LEFTBACK = 15; // has encoder
	
	//TeleOp
	public static final int XB_POS_DRIVER = 0;
	public static final int XB_POS_MANIP = 1;

	public static double kP = 0.1; 
    public static double kI = 1e-4;
    public static double kD = 1; 
    public static double kIz = 0;
    public static double kFF = 0;
    public static double kMaxOutput = 1.0;
    public static double kMinOutput = -1.0;
}

