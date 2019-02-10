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

    public static void setPosition(int elevatorPosition){
        elevatorPIDController.setReference(elevatorPosition, ControlType.kSmartMotion);
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

    public static void flipClawDown(){
        lHatchesFlip.set(false);
    }

    public static void setFrontHolderForward(double power){
        cargoHolderFront.set(ControlMode.PercentOutput, power);
    }

    public static void setFrontHolderBackward(double power){
        cargoHolderFront.set(ControlMode.PercentOutput, -power);
    }

    public static void setBackHolderForward(double power){
        cargoHolderBack.set(ControlMode.PercentOutput, power);
    }

    public static void setBackHolderBackward(double power){
        cargoHolderBack.set(ControlMode.PercentOutput, -power);
    }
    
    //Diagnostic Information
    public static double getElevatorTemp(){
        return elevatorSparkRight.getMotorTemperature();
    }

    public static void setPower(double power){
        elevatorPIDController.setReference(power, ControlType.kDutyCycle);
    }

    public static double getAmps(){
        return elevatorSparkLeft.getOutputCurrent();
    }
 }