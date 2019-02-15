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
        pushDouble("percentElevator", Elevator.getPercentOutput());
        pushDouble("velElevator", Elevator.getVelocity());
        pushDouble("posElevator", Elevator.getPosition());
    }

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

    public static void pushIngestorDiagnostics(){
        diagnosticTable.getEntry("beltAlive").setBoolean(Ingestor.isBeltTalonAlive());
        diagnosticTable.getEntry("cargoAlive").setBoolean(Ingestor.isCargoIngestorAlive());

        pushDouble("cargoIngestorTemp", Ingestor.getTempCargoIngestor());
        pushDouble("beltTemp", Ingestor.getTempBelt());
    }

    static void pushDouble(String name, double value){
        diagnosticTable.getEntry(name).setDouble(value);

    }
}