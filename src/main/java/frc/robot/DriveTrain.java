package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

public class DriveTrain implements PIDOutput{
    private static CANSparkMax rightMotorFront, rightMotorMiddle, rightMotorBack, leftMotorFront, leftMotorMiddle, leftMotorBack;
    private static CANPIDController pidControllerLeftFront, pidControllerRightFront;
    private static CANEncoder encoderLeftFront, encoderRightFront;
    private static AHRS hyro;
    private static Solenoid shifter;
    private static AnuragPIDController hyropid;
    public static DriveTrain instance = null;
    
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

        //Left SPARK Motor
        leftMotorFront = new CANSparkMax(Constants.DT_TALON_LEFTFRONT, MotorType.kBrushless);
        leftMotorMiddle = new CANSparkMax(Constants.DT_TALON_LEFTMIDDLE, MotorType.kBrushless);
        leftMotorBack = new CANSparkMax(Constants.DT_TALON_LEFTBACK, MotorType.kBrushless);

        //Gyro Instantiation Plus PID
        hyro = new AHRS(SPI.Port.kMXP);
        hyropid = new AnuragPIDController(Constants.LIMELIGHT_P, Constants.LIMELIGHT_I, Constants.LIMELIGHT_D, hyro, this);
        hyropid.setInputRange(-180d, 180d);
        hyropid.setOutputRange(-1.0, 1.0);

        hyropid.setAbsoluteTolerance(2d);
        hyropid.setContinuous(true);

        //Shifter
        shifter = new Solenoid(Constants.SOLENOID_SHIFTER);
        
        //PIDController for SparkMax
        pidControllerRightFront = rightMotorFront.getPIDController();
        pidControllerLeftFront = leftMotorFront.getPIDController();
        

        //Getting Encoders for SparkMax
        encoderRightFront = rightMotorFront.getEncoder();
        encoderLeftFront = leftMotorFront.getEncoder();

        //Setting P, I, D in to SparkMaxes
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
        DriveTrain.setAllCoast();
        
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
		drive(Utils.ensureRange(fwd + tur, -1d, 1d), Utils.ensureRange(fwd - tur, -1d, 1d));
    }

    public static double getAHRS(){
        return hyro.getAngle();
    }

    public static void shiftUp(){
        shifter.set(true);
    }

    public static void shiftDown(){
        shifter.set(false);
    }

    public static void setAllCoast(){
        rightMotorFront.setIdleMode(IdleMode.kCoast);
        rightMotorMiddle.setIdleMode(IdleMode.kCoast);
        rightMotorBack.setIdleMode(IdleMode.kCoast);

        leftMotorFront.setIdleMode(IdleMode.kCoast);
        leftMotorMiddle.setIdleMode(IdleMode.kCoast);
        leftMotorBack.setIdleMode(IdleMode.kCoast);
    }

    public static void setAllBreak(){
        rightMotorFront.setIdleMode(IdleMode.kBrake);
        rightMotorMiddle.setIdleMode(IdleMode.kBrake);
        rightMotorBack.setIdleMode(IdleMode.kBrake);

        leftMotorFront.setIdleMode(IdleMode.kBrake);
        leftMotorMiddle.setIdleMode(IdleMode.kBrake);
        leftMotorBack.setIdleMode(IdleMode.kBrake);
    }
    public static boolean getShifted(){
        return shifter.get();
    }

    public static double getEncoderRight(){
        return encoderRightFront.getPosition();
    }

    public static double getEncoderLeft(){
        return encoderLeftFront.getPosition();
    }

    public static double getVelocityRight(){
        return encoderLeftFront.getVelocity();
    }

    public static double getVelocityLeft(){
        return encoderLeftFront.getVelocity();
    }

    public static double getAvgVelocity(){
        return encoderLeftFront.getVelocity();
    }

    //Turn To Angle
    public static void turnToAngle(double angle){
		hyropid.setSetpoint(angle);
		if(!hyropid.isEnabled()){
			//System.out.println("PID Enabled");
			hyropid.reset();
			hyropid.enable();
		}	
    }
    
    
	@Override
	public void pidWrite(double output) {
        System.out.println("PID Wrote " + output);
		if (Math.abs(hyropid.getError()) < 5d) {
			hyropid.setPID(hyropid.getP(), .001, 0);
		} else {
			// I Zone
			hyropid.setPID(hyropid.getP(), 0, 0);
        }
        DriveTrain.arcadeDrive(output, 0);
	}

    public static void pidDisable(){
        System.out.println("PID Disabled");
        hyropid.disable();
    }

    public static void pidEnable(){
        hyropid.enable();
    }

    public static boolean ispidEnabled(){
        return hyropid.isEnabled();
    }

    //Diagnostics
    public static double getRightMotorFrontTemp(){
        return rightMotorFront.getMotorTemperature();
    }

    public static double getPIDError(){
        System.out.println("ERROR: " + hyropid.getError());
        return hyropid.getError();
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

    public static void driveStraight(double power){
		//DriveTrain.drive(power * Constants.DRIVE_STRAIGHT_CONSTANT, -power);
        Limelight.PID(DriveTrain.getAHRS());
        DriveTrain.arcadeDrive(power, Limelight.output);
	}
}