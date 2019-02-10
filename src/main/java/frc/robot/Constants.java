package frc.robot;

public class Constants {
	/**
	 * Use This File Only For Emergencies (like JSONConstants cannot find the config file)
	 */
	//Drive Train
	public static final int DT_TALON_RIGHTFRONT = 2; // has encoder
	public static final int DT_TALON_RIGHTMIDDLE = 1;
	public static final int DT_TALON_RIGHTBACK = 20;

	public static final int DT_TALON_LEFTFRONT = 13;
	public static final int DT_TALON_LEFTMIDDLE = 14;
	public static final int DT_TALON_LEFTBACK = 15; // has encoder
	
	//Ingestor
	public static final int INGESTOR_TALON_CARGO = 10;
	public static final int INGESTOR_TALON_BELT = 11;

	public static final double INGESTOR_BELT_POWER = -1;

	//Elevator
	public static int ELEVATOR_SPARKMAX_LEFT = 12;
	public static int ELEVATOR_SPARKMAX_RIGHT = 3;


	public static double ELEVATOR_kP = 5E-5;
	public static double ELEVATOR_kI = 1E-6;
	public static double ELEVATOR_kD = 0;
	public static double ELEVATOR_kFF = 0;
	public static double ELEVATOR_kIZ = 0;

	public static double ELEVATOR_MAX_OUTPUT = 1.0;
	public static double ELEVATOR_MIN_OUTPUT = -1.0;

	public static double ELEVATOR_MAX_RPM = 5700;

	public static double ELEVATOR_MAX_VEL = 2000;
	public static double ELEVATOR_MIN_VEL = 0;

	public static double ELEVATOR_MAX_ACC = 1500;
	public static double ELEVATOR_MIN_ACC = 0;

	
	public static double ELEVATOR_ALLOWED_ERR = 0;

	public static int ELEVATOR_SMART_MOTION_SLOT = 0;

	public static int ELEVATOR_TALON_CARGO_HOLDER_FRONT = 5;
	public static int ELEVATOR_TALON_CARGO_HOLDER_BACK = 6;

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

	public static int SOLENOID_LHATCHES_CLOSE = 0;
	public static int SOLENOID_LHATCHES_DEPLOY = 1;
	public static int SOLENOID_SHIFTER = 2;
	public static int SOLENOID_ROLLERBAR = 3;

	//public static int SOLENOID_LHATCHES_FLIP = 2;
	public static int[] SOLENOID_4_5 = {4, 5};

	public static int XBOX_DRIVER = 0;
	public static int XBOX_MANIP = 1;
}

