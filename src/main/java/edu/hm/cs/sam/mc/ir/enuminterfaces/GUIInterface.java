package edu.hm.cs.sam.mc.ir.enuminterfaces;

/**
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public interface GUIInterface {

    public void printConsole(String text);

    public void setDroneConnectionColor(ColorEnum setColor);

    public void setMissionButtonEnabled(TasksEnum task, boolean enable);
    
    public void setStaticTargetCoordinates(double lat, double lng);
    
    public void setDynamicTargetCoordinates(double lat, double lng);
    
    public void taskCancelled(TasksEnum task, boolean success);

    public void setStaticTaskActive(boolean taskActive);
    
    public void setDynamicTaskActive(boolean taskActive);
    
    public void setEmergentTaskActive(boolean taskActive);

}
