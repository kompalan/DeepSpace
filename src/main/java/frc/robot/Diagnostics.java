package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Diagnostics{
    private static Diagnostics instance;
    private static NetworkTableInstance inst;
    private static NetworkTable diagnosticTable;
    private static SmartDashboard tuningDashboard;

    public static Diagnostics getInstance(){
        if(instance == null){
            instance = new Diagnostics();
        }

        return instance;
    }

    public Diagnostics(){
        inst = NetworkTableInstance.getDefault();
        diagnosticTable = inst.getTable("datatable");
    }

    public static void pushElevatorDiagnostics(){
        pushDouble("elLeftTemp",Elevator.getLeftMotorTemperature());
        pushDouble("elRightTemp",Elevator.getRightMotorTemperature());

        pushDouble("elLeftCurrent",Elevator.getLeftMotorCurrent());
        pushDouble("elRightCurrent",Elevator.getRightMotorCurrent());
    }

    public static void pushDriveTrainDiagnostics(){
        diagnosticTable.getEntry("dtRightFrontTemp").setDouble(DriveTrain.getRightMotorFrontTemp());
        diagnosticTable.getEntry("dtRightMiddleTemp").setDouble(DriveTrain.getRightMotorMiddleTemp());
        diagnosticTable.getEntry("dtRightBackTemp").setDouble(DriveTrain.getRightMotorBackTemp());

        diagnosticTable.getEntry("dtLeftFrontTemp").setDouble(DriveTrain.getLeftMotorFrontTemp());
        diagnosticTable.getEntry("dtLeftMiddleTemp").setDouble(DriveTrain.getLeftMotorMiddleTemp());
        diagnosticTable.getEntry("dtLeftBackTemp").setDouble(DriveTrain.getLeftMotorBackTemp());

        pushDouble("dtRightFrontCurrent",DriveTrain.getRightMotorFrontCurrent());
        pushDouble("dtRightMiddleCurrent",DriveTrain.getRightMotorMiddleCurrent());
        pushDouble("dtRightBackCurrent",DriveTrain.getRightMotorBackCurrent());

        pushDouble("dtLeftFrontCurrent",DriveTrain.getLeftMotorFrontCurrent());
        pushDouble("dtLeftMiddleCurrent",DriveTrain.getLeftMotorMiddleCurrent());
        pushDouble("dtLeftBackCurrent",DriveTrain.getLeftMotorBackCurrent());

    }

    public static void pushIngestorDiagnostics(){
        diagnosticTable.getEntry("cargoIngestorTemp").setDouble(Ingestor.getTempCargoIngestor());
        diagnosticTable.getEntry("beltTemp").setDouble(Ingestor.getTempBelt());
        diagnosticTable.getEntry("beltAlive").setBoolean(Ingestor.isBeltTalonAlive());
        diagnosticTable.getEntry("cargoAlive").setBoolean(Ingestor.isCargoIngestorAlive());
    }

    static void pushDouble(String name, double value){
        diagnosticTable.getEntry(name).setDouble(value);

    }

}