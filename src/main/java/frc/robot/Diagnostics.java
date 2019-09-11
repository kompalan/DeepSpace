package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Diagnostics{
    private static Diagnostics instance;
    private static NetworkTableInstance inst;
    private static NetworkTable diagnosticTable;
    private static SmartDashboard tuningDashboard;
    private static PowerDistributionPanel pdp;
    
    /**
     * <summary>
     * The idea of Diagnostics is to Push Relevant Values to Network Tables and
     * ShuffleBoard in order to add visibility on the user end
     * 
     * Diagnostics is seperated into three main parts:
     * 1. Subsystems Residing on the DriveTrain (NEO Temps/Speeds)
     * 2. Subsystems Residing on the Elevator (Elevator Positions and Velocities)
     * 3. Subsystems Residing on the Ingestor (Belt and Intake Statuses)
     */

    public static Diagnostics getInstance(){
        if(instance == null){
            instance = new Diagnostics();
        }

        return instance;
    }

    public Diagnostics(){
        inst = NetworkTableInstance.getDefault();
        diagnosticTable = inst.getTable("datatable");
        pdp=new PowerDistributionPanel();
        inst.setUpdateRate(0.01);

    }
    
    //Elevator Temps, Current and PID Information
    public static void pushElevatorDiagnostics(){
        pushDouble("elLeftTemp",Elevator.getLeftMotorTemperature());
        pushDouble("elRightTemp",Elevator.getRightMotorTemperature());

        pushDouble("elLeftCurrent",pdp.getCurrent(Constants.ELEVATOR_SPARKMAX_LEFT));
        pushDouble("elRightCurrent",pdp.getCurrent(Constants.ELEVATOR_SPARKMAX_RIGHT));
        pushDouble("percentElevator", Elevator.getPercentOutput());
        pushDouble("velElevator", Elevator.getVelocity());
        pushDouble("posElevator", Elevator.getPosition());

        pushDouble("ArbFFVolts", Elevator.getArbitraryFeedForward());
        pushDouble("OutputVolt", Elevator.getOutputVoltage());
        pushDouble("Elevator kD", Elevator.getElevatorD());
        pushDouble("Elevator kI", Elevator.getElevatorI());
        pushDouble("Elevator kP", Elevator.getElevatorP());
        pushDouble("Elevator kFF", Elevator.getElevatorFF());
        
    }

    //Temps and Currents of NEO's
    public static void pushDriveTrainDiagnostics(){
        pushDouble("dtRightFrontTemp", DriveTrain.getRightMotorFrontTemp());
        pushDouble("dtRightMiddleTemp", DriveTrain.getRightMotorMiddleTemp());
        pushDouble("dtRightBackTemp", DriveTrain.getRightMotorBackTemp());

        pushDouble("dtLeftFrontTemp", DriveTrain.getLeftMotorFrontTemp());
        pushDouble("dtLeftMiddleTemp", DriveTrain.getLeftMotorMiddleTemp());
        pushDouble("dtLeftBackTemp", DriveTrain.getLeftMotorBackTemp());
        
        pushDouble("dtRightFrontCurrent",DriveTrain.getRightMotorFrontCurrent());
        pushDouble("dtRightMiddleCurrent",DriveTrain.getRightMotorMiddleCurrent());
        pushDouble("dtRightBackCurrent",DriveTrain.getRightMotorBackCurrent());

        pushDouble("dtLeftFrontCurrent",DriveTrain.getLeftMotorFrontCurrent());
        pushDouble("dtLeftMiddleCurrent",DriveTrain.getLeftMotorMiddleCurrent());
        pushDouble("dtLeftBackCurrent",DriveTrain.getLeftMotorBackCurrent());
        pushDouble("dtVelocity", DriveTrain.getAvgVelocity());
    }

    //Statuses of Ingestors and Belt
    public static void pushIngestorDiagnostics(){
        diagnosticTable.getEntry("beltAlive").setBoolean(Ingestor.isBeltTalonAlive());
        diagnosticTable.getEntry("cargoAlive").setBoolean(Ingestor.isCargoIngestorAlive());

        pushDouble("cargoIngestorTemp", Ingestor.getTempCargoIngestor());
        pushDouble("beltTemp", Ingestor.getTempBelt());
    }

    public static void pushErrorDiagnostics(){
        pushDouble("error", Elevator.getError());
        inst.flush();
    }
    public static void pushDouble(String name, double value){
        diagnosticTable.getEntry(name).setDouble(value);

    }

}