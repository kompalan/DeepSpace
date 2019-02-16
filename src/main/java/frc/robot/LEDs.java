package frc.robot;

import edu.wpi.first.wpilibj.Spark;

public class LEDs {
    private static Spark leds;
    private static LEDs instance;

    public static LEDs getInstance(){
        if(leds == null){
            instance = new LEDs();
        }

        return instance;
    }

    private LEDs(){
        leds = new Spark(Constants.LED_CHANNEL);
    }

    public static void setOrange(){
        leds.set(0.65);
    }

    public static void setLime(){
        leds.set(0.73);
    }

    public static void setNeutral(){
        leds.set(-0.99);
    }


}