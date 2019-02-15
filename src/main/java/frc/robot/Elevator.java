package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * <summary>
 * Code referring to Elevator
 * </summary>
 */
 
 public class Elevator{
    private static Elevator instance = null;

    //Stuff Dealing with Elevator
    private static CANSparkMax elevatorSparkLeft, elevatorSparkRight;
    private static CANPIDController elevatorPIDController;
    private static CANEncoder elevatorEncoder;

    private static Solenoid lHatchesClaw, lHatchesFlip;

    private static TalonSRX cargoHolderFront, cargoHolderBack;

    public static Elevator getInstance(){
        if(instance == null){
            instance = new Elevator();
        }

        return instance;
    }
    
    
    public Elevator(){
        elevatorSparkRight = new CANSparkMax(Constants.ELEVATOR_SPARKMAX_RIGHT, MotorType.kBrushless);
        elevatorSparkLeft = new CANSparkMax(Constants.ELEVATOR_SPARKMAX_LEFT, MotorType.kBrushless);
        
        cargoHolderFront = new TalonSRX(Constants.ELEVATOR_TALON_CARGO_HOLDER_FRONT);
        cargoHolderBack = new TalonSRX(Constants.ELEVATOR_TALON_CARGO_HOLDER_BACK);

        lHatchesClaw = new Solenoid(Constants.SOLENOID_LHATCHES_CLOSE);
        lHatchesFlip = new Solenoid(Constants.SOLENOID_LHATCHES_DEPLOY);

        elevatorPIDController = elevatorSparkRight.getPIDController();
        elevatorEncoder = elevatorSparkRight.getEncoder();

        elevatorPIDController.setP(Constants.ELEVATOR_kP);
        elevatorPIDController.setI(Constants.ELEVATOR_kI);
        elevatorPIDController.setD(Constants.ELEVATOR_kD);
        elevatorPIDController.setFF(Constants.ELEVATOR_kFF);
        elevatorPIDController.setIZone(Constants.ELEVATOR_kIZ);
        elevatorPIDController.setOutputRange(Constants.ELEVATOR_MIN_OUTPUT, Constants.ELEVATOR_MAX_OUTPUT);

        elevatorPIDController.setSmartMotionMaxVelocity(Constants.ELEVATOR_MAX_VEL, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionMinOutputVelocity(Constants.ELEVATOR_MIN_VEL, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionMaxAccel(Constants.ELEVATOR_MAX_ACC, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionAllowedClosedLoopError(Constants.ELEVATOR_ALLOWED_ERR, Constants.ELEVATOR_SMART_MOTION_SLOT);
        
        elevatorSparkLeft.follow(elevatorSparkRight, true);
    }

    public static void setPosition(double elevatorPosition){
        elevatorPIDController.setReference(elevatorPosition, ControlType.kSmartMotion);
    }

    public static void stallElevator(){
        elevatorPIDController.setReference(elevatorEncoder.getPosition(), ControlType.kPosition);
    }

    public static double getPosition(){
        return elevatorEncoder.getPosition();
    }

    public static void setClawIn(){
        lHatchesClaw.set(true);
    }

    public static void setClawOut(){
        lHatchesClaw.set(false);
    }

    public static void flipClawUp(){
        lHatchesFlip.set(true);
    }

    public static boolean getFlipped(){
        return lHatchesFlip.get();
    }

    public static void flipClawDown(){
        lHatchesFlip.set(false);
    }

    public static void setFrontHolderPower(double power){
        cargoHolderFront.set(ControlMode.PercentOutput, power);
    }
    public static void setBackHolderPower(double power){
        cargoHolderBack.set(ControlMode.PercentOutput, power);
    }
    
    //Diagnostic Information
    public static void setPower(double power){
        elevatorPIDController.setReference(power, ControlType.kDutyCycle);
    }

    public static double getLeftMotorCurrent(){
        return elevatorSparkLeft.getOutputCurrent();
    }
    public static double getLeftMotorTemperature(){
        return elevatorSparkLeft.getMotorTemperature();
    }
    public static double getRightMotorCurrent(){
        return elevatorSparkRight.getOutputCurrent();
    }
    public static double getRightMotorTemperature(){
        return elevatorSparkRight.getMotorTemperature();
    }

    public static double getPercentOutput(){
        return elevatorSparkRight.getAppliedOutput();
    }

    public static double getVelocity(){
        return elevatorEncoder.getVelocity();
    }

    //FOR TESTING PURPOSES ONLY!! REMOVE BEFORE COMP
    public static void setElevatorPID(double kP, double kI, double kD, double kFF, double kIz, double kMinOut, double kMaxOut){
        elevatorPIDController.setP(kP);
        elevatorPIDController.setI(kI);
        elevatorPIDController.setD(kD);
        elevatorPIDController.setFF(kFF);
        elevatorPIDController.setIZone(kIz);
        elevatorPIDController.setOutputRange(kMinOut, kMaxOut);
    }

    public static void setElevatorSmartMotion(double minVel, double maxVel, double minAcc, double maxAcc, double allowedErr){
        elevatorPIDController.setSmartMotionMaxVelocity(maxVel, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionMinOutputVelocity(minVel, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionMaxAccel(maxAcc, Constants.ELEVATOR_SMART_MOTION_SLOT);
        elevatorPIDController.setSmartMotionAllowedClosedLoopError(allowedErr, Constants.ELEVATOR_SMART_MOTION_SLOT);
    }

    public static void setELEVATOR_P(double kP){
        elevatorPIDController.setP(kP);

    }

    public static void setELEVATOR_I(double kI){
        elevatorPIDController.setI(kI);

    }

    public static void setELEVATOR_D(double kD){
        elevatorPIDController.setD(kD);

    }

    public static void setELEVATOR_FF(double kFF){
        elevatorPIDController.setFF(kFF);

    }

    public static void setELEVATOR_IZ(double kIZ){
        elevatorPIDController.setIZone(kIZ);
    }

    public static void setELEVATOR_KOUTPUT(double kMinOut, double kMaxOut){
        elevatorPIDController.setOutputRange(kMinOut, kMaxOut);

    }

    public static void setELEVATOR_MAXVEL(double maxVel){
        elevatorPIDController.setSmartMotionMaxVelocity(maxVel, Constants.ELEVATOR_SMART_MOTION_SLOT);

    }

    public static void setELEVATOR_MINVEL(double minVel){
        elevatorPIDController.setSmartMotionMinOutputVelocity(minVel, Constants.ELEVATOR_SMART_MOTION_SLOT);

    }

    public static void setELEVATOR_MAXACC(double maxAcc){
        elevatorPIDController.setSmartMotionMaxAccel(maxAcc, Constants.ELEVATOR_SMART_MOTION_SLOT);
    }

    public static void setELEVATOR_MAXERR(double maxerr){
        elevatorPIDController.setSmartMotionAllowedClosedLoopError(maxerr, Constants.ELEVATOR_SMART_MOTION_SLOT);
    }

    public static double getElevatorEncoder(){
        return elevatorEncoder.getPosition();
    }

    public static void setElevatorEncoder(){
        elevatorPIDController.setReference(5.00, ControlType.kPosition);
    }
 }