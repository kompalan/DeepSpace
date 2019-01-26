package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

public class DriveTrain {
    private static CANSparkMax rightMotorFront, rightMotorMiddle, rightMotorBack, leftMotorFront, leftMotorMiddle, leftMotorBack;
    private static CANPIDController pidControllerLeftFront, pidControllerRightFront;
    private static CANEncoder encoderLeftFront, encoderRightFront;

    public static DriveTrain instance;
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
        rightMotorFront = new CANSparkMax((int) JSONConstants.get("driveTrainRightFront"), MotorType.kBrushless);
        rightMotorMiddle = new CANSparkMax((int) JSONConstants.get("driveTrainRightMiddle"), MotorType.kBrushless);
        rightMotorBack = new CANSparkMax((int) JSONConstants.get("driveTrainRightBack"), MotorType.kBrushless);


        leftMotorFront = new CANSparkMax((int) JSONConstants.get("driveTrainLeftFront"), MotorType.kBrushless);
        leftMotorMiddle = new CANSparkMax((int) JSONConstants.get("driveTrainLeftMiddle"), MotorType.kBrushless);
        leftMotorBack = new CANSparkMax((int) JSONConstants.get("driveTrainLeftBack"), MotorType.kBrushless);

        pidControllerRightFront = rightMotorFront.getPIDController();
        pidControllerLeftFront = leftMotorFront.getPIDController();
        

        encoderRightFront = rightMotorFront.getEncoder();
        encoderLeftFront = leftMotorFront.getEncoder();


        pidControllerRightFront.setP((double)JSONConstants.get("SPARK_kP_RIGHT"));
        pidControllerRightFront.setI((double)JSONConstants.get("SPARK_kI_RIGHT"));
        pidControllerRightFront.setD((double)JSONConstants.get("SPARK_kD_RIGHT"));
        pidControllerRightFront.setIZone((double)JSONConstants.get("SPARK_kIz_RIGHT"));

        pidControllerLeftFront.setP((double)JSONConstants.get("SPARK_kP_LEFT"));
        pidControllerLeftFront.setI((double)JSONConstants.get("SPARK_kI_LEFT"));
        pidControllerLeftFront.setD((double)JSONConstants.get("SPARK_kD_LEFT"));
        pidControllerLeftFront.setIZone((double)JSONConstants.get("SPARK_kIz_LEFT"));

        pidControllerLeftFront.setOutputRange((double)JSONConstants.get("SPARK_kMinOutput"), (double)JSONConstants.get("SPARK_kMaxOutput"));
        pidControllerRightFront.setOutputRange((double)JSONConstants.get("SPARK_kMinOutput"),(double)JSONConstants.get("SPARK_kMaxOutput"));

        rightMotorMiddle.follow(rightMotorFront);
        rightMotorBack.follow(rightMotorFront);
        
        leftMotorMiddle.follow(leftMotorFront);
        leftMotorBack.follow(leftMotorFront);
    }

    public static void drive(double powerLeft, double powerRight){

        //Set Power To Front Motors (Others are set to Follow Front Motors)
        // rightMotorFront.set(powerRight);
        // leftMotorFront.set(powerLeft);
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

    public static double getEncoderRight(){
        return encoderRightFront.getPosition();
    }

    public static double getEncoderLeft(){
        return encoderLeftFront.getPosition();
    }


}