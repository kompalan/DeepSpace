package frc.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Spark;

public class LEDs {
    private static Spark leds;
    private static LEDs instance;

    private static HashMap<String, Double> colorArray;


    public static LEDs getInstance(){
        if(leds == null){
            instance = new LEDs();
        }

        return instance;
    }

    private LEDs(){
        leds = new Spark(Constants.LED_CHANNEL);
        colorArray = new HashMap<String, Double>();

        colorArray.put("Lime", 0.77);
        colorArray.put("Orange", 0.65);
        colorArray.put("Violet", 0.91);
        colorArray.put("Rainbow", -0.99);
        colorArray.put("Larson Scanner", -0.01); 
        colorArray.put("Red Strobe", -0.11);
        colorArray.put("White Strobe", -0.05);
        colorArray.put("Lava Strobe", -0.39);
        colorArray.put("Blue", 0.85);
        colorArray.put("Red", 0.61);
        colorArray.put("Black", 0.99);
    }

    public static void setLava(){
        leds.set(colorArray.get("Lava Strobe"));
    }

    public static void setOrange(){
        leds.set(colorArray.get("Orange"));
    }

    public static void setLime(){
        leds.set(colorArray.get("Lime"));
    }

    public static void setNeutral(){
        leds.set(colorArray.get("Rainbow"));
    }

    public static void setLarson(){
        leds.set(colorArray.get("Larson Scanner"));
    }

    public static void setViolet(){
        leds.set(colorArray.get("Violet"));
    }

    public static void setRedStrobe(){
        leds.set(colorArray.get("Red Strobe"));
    }

    public static void setWhiteStrobe(){
        leds.set(colorArray.get("White Strobe"));
    }

    public static void setBlue(){
        leds.set(colorArray.get("Red"));
    }

    public static void setRed(){
        leds.set(colorArray.get("Blue"));
    }

    public static void setBlack(){
        leds.set(colorArray.get("Black"));
    }

}