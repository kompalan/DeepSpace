package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.ControlType;

public class DriveTrain {
    private static CANSparkMax rightMotorFront, rightMotorMiddle, rightMotorBack, leftMotorFront, leftMotorMiddle, leftMotorBack;
    private static CANPIDController pidControllerLeftFront, pidControllerRightFront;
    private static CANEncoder encoderLeftFront, encoderRightFront;
    private static AHRS hyro;
    private static Solenoid shifter;

    public static DriveTrain instance = null;
    //public static Limelight limelight;

    /**
     * TODO: Move to config.json
     */


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

        hyro = new AHRS(SPI.Port.kMXP);

        shifter = new Solenoid(Constants.SOLENOID_SHIFTER);
        
        pidControllerRightFront = rightMotorFront.getPIDController();
        pidControllerLeftFront = leftMotorFront.getPIDController();
        

        encoderRightFront = rightMotorFront.getEncoder();
        encoderLeftFront = leftMotorFront.getEncoder();


        pidControllerRightFront.setP(Constants.kP);
        pidControllerRightFront.setI(Constants.kI);
        pidControllerRightFront.setD(Constants.kD);
        pidControllerRightFront.setIZone(Constants.kIz);

        pidControllerLeftFront.setP(Constants.kP);
        pidControllerLeftFront.setI(Constants.kI);
        pidControllerLeftFront.setD(Constants.kD);
        pidControllerLeftFront.setIZone(Constants.kIz);

        pidControllerLeftFront.setOutputRange(Constants.kMinOutput, Constants.kMaxOutput);
        pidControllerRightFront.setOutputRange(Constants.kMinOutput, Constants.kMaxOutput);

        rightMotorMiddle.follow(rightMotorFront);
        rightMotorBack.follow(rightMotorFront);
        
        leftMotorMiddle.follow(leftMotorFront);
        leftMotorBack.follow(leftMotorFront);
    }

    public static void drive(double powerLeft, double powerRight){
        pidControllerRightFront.setReference(powerRight, ControlType.kDutyCycle);
        pidControllerLeftFront.setReference(powerLeft, ControlType.kDutyCycle);
    }

    public static void arcadeDrive(double fwd, double tur) {
        //Arcade Drive
		if (Math.abs(tur) < .01)
			tur = 0;
		if (Math.abs(fwd) < .01)
            fwd = 0;
        
		drive(Utils.ensureRange(fwd + tur, -1d, 1d), Utils.ensureRange(fwd - tur, -1d, 1d));
    }

    public static void shiftUp(){
        shifter.set(true);
    }

    public static void shiftDown(){
        shifter.set(false);
    }

    public static double getEncoderRight(){
        return encoderRightFront.getPosition();
    }

    public static double getEncoderLeft(){
        return encoderLeftFront.getPosition();
    }

    //Diagnostics
    public static double getRightMotorFrontTemp(){
        return rightMotorFront.getMotorTemperature();
    }

    public static double getRightMotorMiddleTemp(){
        return rightMotorMiddle.getMotorTemperature();
    }

    public static double getRightMotorBackTemp(){
        return rightMotorBack.getMotorTemperature();
    }

    public static double getLeftMotorFrontTemp(){
        return leftMotorFront.getMotorTemperature();
    }

    public static double getLeftMotorMiddleTemp(){
        return leftMotorMiddle.getMotorTemperature();
    }

    public static double getLeftMotorBackTemp(){
        return leftMotorBack.getMotorTemperature();
    }
    public static double getRightMotorFrontCurrent(){
        return rightMotorFront.getOutputCurrent();
    }

    public static double getRightMotorMiddleCurrent(){
        return rightMotorMiddle.getOutputCurrent();
    }

    public static double getRightMotorBackCurrent(){
        return rightMotorBack.getOutputCurrent();
    }

    public static double getLeftMotorFrontCurrent(){
        return leftMotorFront.getOutputCurrent();
    }

    public static double getLeftMotorMiddleCurrent(){
        return leftMotorMiddle.getOutputCurrent();
    }

    public static double getLeftMotorBackCurrent(){
        return leftMotorBack.getOutputCurrent();
    }
}