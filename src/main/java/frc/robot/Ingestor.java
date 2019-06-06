package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * <summary>
 * Code meant for the Cargo Ingestor/Cargo Delivery System
 * </summary>
 */
public class Ingestor{
    private static Ingestor instance;
    private static TalonSRX cargoIngestorTalon;
    private static TalonSRX beltTalon;
    private static Solenoid ingestor;

    public static Ingestor getInstance(){
        if(instance == null){
            instance = new Ingestor();
        }

        return instance;
    }

    //Initializes the talons and the solenoid on the ingestor
    public Ingestor(){
        cargoIngestorTalon = new TalonSRX(Constants.INGESTOR_TALON_CARGO);
        beltTalon = new TalonSRX(Constants.INGESTOR_TALON_BELT);
        ingestor = new Solenoid(Constants.SOLENOID_ROLLERBAR);
    }

    //Ingests cargo
    public static void ingestCargo(double power){
        cargoIngestorTalon.set(ControlMode.PercentOutput, power);
    }

    //Exgests cargo
    public static void exgestCargo(double power){
        cargoIngestorTalon.set(ControlMode.PercentOutput, -power);
    }

    //Moves the cargo belt from the ingestor to the elevator
    public static void beltUp(){
        beltTalon.set(ControlMode.PercentOutput, Constants.INGESTOR_BELT_POWER);
    }

    //Stops the belt
    public static void beltStop(){
        beltTalon.set(ControlMode.PercentOutput, 0);
    }

    //Moves the cargo belt from the elevator to the ingestor
    public static void beltDown(){
        beltTalon.set(ControlMode.PercentOutput, -Constants.INGESTOR_BELT_POWER);
    }
    
    //Puts down the ingestor
    public static void ingestorDown(){
        ingestor.set(true);
    }

    //Raises the ingestor
    public static void ingestorUp(){
        ingestor.set(false);
    }

    //Returns true of the ingestor is up
    public static boolean isIngestorUp(){
        return ingestor.get();
    }

    //Diagnostic Information
    public static boolean isCargoIngestorAlive(){
        return (cargoIngestorTalon.getBusVoltage() != 0.0);
    }

    public static boolean isBeltTalonAlive(){
        return (beltTalon.getBusVoltage() != 0.0);
    }
    
    public static double getTempCargoIngestor(){
        return cargoIngestorTalon.getTemperature();
    }

    public static double getTempBelt(){
        return beltTalon.getTemperature();
    }
}