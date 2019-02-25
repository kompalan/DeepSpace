package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

public class DriveTrain{
    private static CANSparkMax rightMotorFront, rightMotorMiddle, rightMotorBack, leftMotorFront, leftMotorMiddle, leftMotorBack;
    private static CANPIDController pidControllerLeftFront, pidControllerRightFront;
    private static CANEncoder encoderLeftFront, encoderRightFront;
    private static AHRS hyro;
    private static Solenoid shifter;
    public static DriveTrain instance = null;

    public static double error, integral, derivative, previous_error, output;
    
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


        //Out motor output range is -100 percent power to 100 percent power
        pidControllerLeftFront.setOutputRange(Constants.kMinOutput, Constants.kMaxOutput);
        pidControllerRightFront.setOutputRange(Constants.kMinOutput, Constants.kMaxOutput);

        //Set all motors to coast mode
        DriveTrain.setAllCoast();
        
        //Set all back and middle motors as a "follower" of front motors
        //This means that the "slaves" inherit almost all properties of its master (powers etc.)
        rightMotorMiddle.follow(rightMotorFront);
        rightMotorBack.follow(rightMotorFront);
        
        leftMotorMiddle.follow(leftMotorFront);
        leftMotorBack.follow(leftMotorFront);

        
    }

    //Drive on Duty Cycle with Powers supplied by the parameters
    public static void drive(double powerLeft, double powerRight){
        pidControllerRightFront.setReference(powerRight, ControlType.kDutyCycle);
        pidControllerLeftFront.setReference(powerLeft, ControlType.kDutyCycle);
    }

    public static void arcadeDrive(double tur, double fwd) {
        //Arcade Drive      
		drive(Utils.ensureRange(fwd + tur, -1d, 1d), Utils.ensureRange(fwd - tur, -1d, 1d));
    }

    public static double getAHRS(){
        //Get Angle of Gyro
        return hyro.getAngle();
    }

    public static void shiftUp(){
        //Set the single solenoid to the "active" state
        shifter.set(true);
    }

    public static void shiftDown(){
        //Set the single solenoid to the "deactive" state
        shifter.set(false);
    }

    //Sets all motors to coast mode, meaning when motors stop, wheels can still move freely
    //Allows our drivers to slide to a destination instead of aiming, which takes time.
    public static void setAllCoast(){
        rightMotorFront.setIdleMode(IdleMode.kCoast);
        rightMotorMiddle.setIdleMode(IdleMode.kCoast);
        rightMotorBack.setIdleMode(IdleMode.kCoast);

        leftMotorFront.setIdleMode(IdleMode.kCoast);
        leftMotorMiddle.setIdleMode(IdleMode.kCoast);
        leftMotorBack.setIdleMode(IdleMode.kCoast);
    }

    //Sets all motors to break mode meaning when motors stop, wheel movement will be restricted
    //This will be set for PID, for accuracy
    public static void setAllBreak(){
        rightMotorFront.setIdleMode(IdleMode.kBrake);
        rightMotorMiddle.setIdleMode(IdleMode.kBrake);
        rightMotorBack.setIdleMode(IdleMode.kBrake);

        leftMotorFront.setIdleMode(IdleMode.kBrake);
        leftMotorMiddle.setIdleMode(IdleMode.kBrake);
        leftMotorBack.setIdleMode(IdleMode.kBrake);
    }

    public static boolean getShifted(){
        //Get the state of the shifter... are we shifted up? or down?
        return shifter.get();
    }

    public static double getEncoderRight(){
        //Get Encoder Position of the Master Encoder
        return encoderRightFront.getPosition();
    }

    public static double getEncoderLeft(){ 
        //Get Encoder Position of the Master Encoder
        return encoderLeftFront.getPosition();
    }

    public static double getVelocityRight(){
        //Get RPM of Master Encoder
        return encoderLeftFront.getVelocity();
    }

    public static double getVelocityLeft(){
        //Get RPM of Master Encoder
        return encoderLeftFront.getVelocity();
    }

    public static double getAvgVelocity(){
        //TODO: Actually Make it an average
        return encoderLeftFront.getVelocity();
    }


    public static void PID(double setpoint){
        //Makeshift PID for turning to an angle sensibly

        //Get Error
        DriveTrain.error = setpoint - DriveTrain.getAHRS();
        
        //Integral term is error over time
		DriveTrain.integral += (error*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
        
        //Derivative is rate of change over loop time
        derivative = (error - DriveTrain.previous_error) / .02;

        //Set the current error as previous error
		DriveTrain.previous_error = DriveTrain.error;
    
        //Set instance variable DriveTrain.output as the output of PID Controller
        DriveTrain.output = Constants.LIMELIGHT_P*error + Constants.LIMELIGHT_I*Limelight.integral + Constants.LIMELIGHT_D*Limelight.derivative;   
    }

    public static void driveStraight(){ 
        //Resets the gyro to zero and continues along the current heading.       
        DriveTrain.resetAHRS();

        DriveTrain.PID(0);

	}
    
    public static void resetAHRS(){
        //Reset the Yaw Value of AHRS to Zero
        hyro.reset();
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