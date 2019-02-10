package frc.robot;

public class Utils {
	public static double ensureRange(double v, double min, double max) {
		return Math.min(Math.max(v, min), max);
	}
	
	public static double negPowTwo(double v) {
		return (v != 0) ? Math.pow(v, 2)*(Math.abs(v)/v) : 0; 
	}
	
	public static double keepRange(double v){
		if (v > 1.0){
			return 1.0;
		} else {
			return v;
		}
			
				
	}

	public static double inchesToEncs(double inches){
		//Based on wheel circumfrence, perform calculation for inches.
		return inches;
	}

	public static double expoDeadzone(double input, double deadzone, double exponent){
		//http://www.mimirgames.com/articles/games/joystick-input-and-using-deadbands/
		if(Math.abs(input)<=deadzone)
			return 0;
		double deadzoned = (input - Math.signum(input) * deadzone)/(1-deadzone);
		System.out.println(deadzoned);
		double expoed = Math.pow(Math.abs(deadzoned), exponent) * Math.signum(deadzoned);

		return expoed;
	}


}