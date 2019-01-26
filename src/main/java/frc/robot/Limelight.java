package frc.robot;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
	NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	public HashMap<String, Double> relevantValues = new HashMap<String, Double>();
    
    
	public void testFeed(){
		double x = table.getEntry("tx").getDouble(0.0);
		double y = table.getEntry("ty").getDouble(0.0);
		if((x <= 0.5) && (x >= -0.5)){
			//System.out.println(table.getEntry("tx").getDouble(0.0));
		}else{
			//System.out.println(table.getEntry("tx").getDouble(0.0));
		}
		
		
	}
	
	public double getX(){
		return table.getEntry("tx").getDouble(0.0);
	}
	
	public double getY(){
		return table.getEntry("ty").getDouble(0.0);
	}
	
	public double getA(){
		return table.getEntry("ta").getDouble(0.0);
	}
	
	public boolean hasValidTargets(){
		if(table.getEntry("tv").getDouble(0.0) == 1){
			return true;
		}
		return false;
	}
	
	public void changePipeline(int pipeline_num){
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		pipeline.setValue(pipeline_num);
	}
	
	public double getContourArea(){
		return table.getEntry("ta0").getDouble(0.0);
	}
	
	public double getContourX(){
		return table.getEntry("ty1").getDouble(0.0);
	}
	
	public double getDistance(){
		double denominator = Math.tan(this.getY());
		
		double distance = 12 / denominator;
		return distance;
	}
	
}
