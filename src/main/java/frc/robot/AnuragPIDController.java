package frc.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class AnuragPIDController extends PIDController{
    
    public AnuragPIDController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output){
        super(Kp, Ki, Kd, source, output);
    }

    @Override
    public void disable(){
        // Ensures m_enabled check and pidWrite() call occur atomically
        m_pidWriteMutex.lock();
        try {
            m_thisMutex.lock();
            try {
                m_enabled = false;
            } finally {
                m_thisMutex.unlock();
            }
        } finally {
            m_pidWriteMutex.unlock();
            
        }
    }
}