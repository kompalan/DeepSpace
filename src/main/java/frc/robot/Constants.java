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

	//Limelight
	public static final int LIMELIGHT_VISION_TARGET = 1;
	public static final int LIMELIGHT_LINEUP_LINE_FRONT = 2;
	public static final int LIMELIGHT_LINEUP_LINE_MIDDLE = 3;
	public static final int LIMELIGHT_LINEUP_LINE_BACK = 4;

	public static final double CAMERA_ANGLE = 15.0;
	public static final double CAMERA_HEIGHT = 15.0;
	
	public static final double LINEUP_FULL_SPEED = 1.0;
	public static final double LINEUP_HALF_SPEED = 0.5;
	public static final double DRIVE_STRAIGHT_CONSTANT = 1;

	//Ingestor
	public static final int INGESTOR_TALON_CARGO = 10;
	public static final int INGESTOR_TALON_BELT = 11;

	public static final double INGESTOR_BELT_POWER = -1;

	//Elevator
	public static int ELEVATOR_SPARKMAX_LEFT = 12;
	public static int ELEVATOR_SPARKMAX_RIGHT = 3;


	public static double ELEVATOR_kP = 0.00025;
	public static double ELEVATOR_kI = 0;
	public static double ELEVATOR_kD = 5;
	public static double ELEVATOR_kFF = 0.00025;
	public static double ELEVATOR_kIZ = 0;

	public static double ELEVATOR_MAX_OUTPUT = 1;
	public static double ELEVATOR_MIN_OUTPUT = -1;

	public static double ELEVATOR_MAX_RPM = 5700;

	public static double ELEVATOR_MAX_VEL = 50;
	public static double ELEVATOR_MIN_VEL = -50;

	public static double ELEVATOR_MAX_ACC = 100;
	public static double ELEVATOR_MIN_ACC = -100;

	public static double ELEVATOR_ARB_FEED_FORWARD = 1.35;
	
	public static  double ELEVATOR_ALLOWED_ERR = 1;

	public static final int ELEVATOR_SMART_MOTION_SLOT = 0;

	public static final int ELEVATOR_TALON_CARGO_HOLDER_FRONT = 5;
	public static final int ELEVATOR_TALON_CARGO_HOLDER_BACK = 6;

	//TeleOp
	public static final int XB_POS_DRIVER = 0;
	public static final int XB_POS_MANIP = 1;

	public static final double kP = 0.1; 
    public static final double kI = 1e-4;
    public static final double kD = 1; 
    public static final double kIz = 0;
    public static final double kFF = 0;
    public static final double kMaxOutput = 1.0;
	public static final double kMinOutput = -1.0;

	public static final int SOLENOID_LHATCHES_CLOSE = 0;
	public static final int SOLENOID_LHATCHES_DEPLOY = 1;
	public static final int SOLENOID_SHIFTER = 2;
	public static final int SOLENOID_ROLLERBAR = 3;

	public static final int[] SOLENOID_4_5 = {4, 5};

	public static final double MAX_RPM_FIRST_GEAR = 4700d;

	public static final int XBOX_DRIVER = 0;
	public static final int XBOX_MANIP = 1;


	public static final int LED_CHANNEL = 1;
	public static final double LIMELIGHT_P = 0.001;
	public static final double LIMELIGHT_I = 0;
	public static final double LIMELIGHT_D = 0;


	public static final double ELEVATOR_SETPOINT_ROCKET_LOW = 13;
	public static final double ELEVATOR_SETPOINT_ROCKET_MIDDLE = 14;
	public static final double ELEVATOR_SETPOINT_ROCKET_HIGH = 15;
	

	public static final double CARGO_SHIP_SETPOINT_HATCH = 13;
	public static final double CARGO_SHIP_SETPOINT_CARGO = 14;
}

