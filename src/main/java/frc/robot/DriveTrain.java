package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

public class DriveTrain {
    private static final int deviceID = 1;
    private static CANSparkMax rightMotorFront, rightMotorMiddle, rightMotorBack, leftMotorFront, leftMotorMiddle, leftMotorBack;

    private static CANPIDController pidControllerLeftFront, pidControllerRightFront, pidControllerLeftMiddle, pidControllerRightMiddle, pidControllerLeftBack, pidControllerRightBack;
    
    private static CANEncoder encoderLeftFront, encoderRightFront;

    private static double kP = 0.1; 
    private static double kI = 1e-4;
    private static double kD = 1; 
    private static double kIz = 0;
    private static double kFF = 0;
    private static double kMaxOutput = 1.0;
    private static double kMinOutput = -1.0;

    public static DriveTrain instance;
    
    public static DriveTrain getInstance(){
        if(instance == null){
            instance = new DriveTrain();
            return instance;
        }

        return instance;
    }

    public DriveTrain() {
        //Right SPARK Motors
        rightMotorFront = new CANSparkMax(Constants.DT_TALON_RIGHTFRONT, MotorType.kBrushless);
        rightMotorMiddle = new CANSparkMax(Constants.DT_TALON_RIGHTMIDDLE, MotorType.kBrushless);
        rightMotorBack = new CANSparkMax(Constants.DT_TALON_RIGHTBACK, MotorType.kBrushless);


        leftMotorFront = new CANSparkMax(Constants.DT_TALON_LEFTFRONT, MotorType.kBrushless);
        leftMotorMiddle = new CANSparkMax(Constants.DT_TALON_LEFTMIDDLE, MotorType.kBrushless);
        leftMotorBack = new CANSparkMax(Constants.DT_TALON_LEFTBACK, MotorType.kBrushless);

        pidControllerRightFront = rightMotorFront.getPIDController();
        pidControllerLeftFront = leftMotorFront.getPIDController();
        

        encoderRightFront = rightMotorFront.getEncoder();
        encoderLeftFront = leftMotorFront.getEncoder();


        pidControllerRightFront.setP(kP);
        pidControllerRightFront.setI(kI);
        pidControllerRightFront.setD(kD);
        pidControllerRightFront.setIZone(kIz);

        pidControllerLeftFront.setP(kP);
        pidControllerLeftFront.setI(kI);
        pidControllerLeftFront.setD(kD);
        pidControllerLeftFront.setIZone(kIz);

        pidControllerLeftFront.setOutputRange(kMinOutput, kMaxOutput);
        pidControllerRightFront.setOutputRange(kMinOutput,kMaxOutput);

        // leftMotorFront.setInverted(true);
        // leftMotorMiddle.setInverted(true);
        // leftMotorBack.setInverted(true);
    }

    public static void drive(double powerLeft, double powerRight){

        //Set Power To Individual Motors (hopefully follower soon)
        rightMotorFront.set(powerRight);
        rightMotorMiddle.set(powerRight);
        rightMotorBack.set(powerRight);

        leftMotorFront.set(powerLeft);
        leftMotorMiddle.set(powerLeft);
        leftMotorBack.set(powerLeft);


        /**
         * TODO: MAKE THIS WORK AND TUNE PID
         */
        // pidControllerLeftFront.setReference(powerLeft, ControlType.kDutyCycle);
        // pidControllerLeftMiddle.setReference(powerLeft, ControlType.kDutyCycle);
        // pidControllerLeftBack.setReference(powerLeft, ControlType.kDutyCycle);
        
        // pidControllerRightFront.setReference(powerRight, ControlType.kDutyCycle);
        // pidControllerRightMiddle.setReference(powerRight, ControlType.kDutyCycle);
        // pidControllerRightBack.setReference(powerRight, ControlType.kDutyCycle);
    }

    public static void arcadeDrive(double fwd, double tur) {
        //Arcade Drive (inverted for controller ease of access)
		if (Math.abs(tur) < .01)
			tur = 0;
		if (Math.abs(fwd) < .2)
            fwd = 0;
		drive(Utils.ensureRange(fwd-tur, -1d, 1d), Utils.ensureRange(fwd + tur, -1d, 1d));
	}
}