package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Constants {
	/**
	 * TODO: MOVE TO CONFIG.JSON 
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
}

