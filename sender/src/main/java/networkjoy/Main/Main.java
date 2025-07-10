package networkjoy.Main;



import java.util.Map;

import networkjoy.Integration.Joystick;
import networkjoy.Integration.Joystick.ReturnedJoyData;

public class Main {
    public static void main(String[] args) throws Exception{
        Joystick.joystickInit();
        Map<String,Integer> connectedJoysticks = Joystick.getAllJoy();
        System.out.println("HEJ");
        for (String key: connectedJoysticks.keySet()){
            System.out.println(key);
            System.out.println(connectedJoysticks.get(key));
        }
        Joystick joystick = new Joystick(0);
        ReturnedJoyData currentPosition;
        currentPosition = joystick.readAllParts();

        System.out.println("\n\n\n");
        for (int i = 0; i<currentPosition.povs.limit();i++) {
            System.out.println(currentPosition.povs.get(i));
        }
        
        
    }
}