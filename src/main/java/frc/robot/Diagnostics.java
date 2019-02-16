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
    private static PowerDistributionPanel   pdp;

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


    public static void pushErrorDiagnostics(){
        pushDouble("error", Elevator.getError());
        inst.flush();
    }
    public static void pushDouble(String name, double value){
        diagnosticTable.getEntry(name).setDouble(value);

    }

}