package networkjoy.Main;



import java.util.Map;

import networkjoy.Controller.Controller;
import networkjoy.Integration.Joystick;
import networkjoy.View.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Joystick.joystickInit();
        Map<String,Integer> connectedJoysticks = Joystick.getAllJoy();
        System.out.println("HEJ");
        for (String key: connectedJoysticks.keySet()){
            System.out.println(key);
            System.out.println(connectedJoysticks.get(key));
        }


        Controller controller = new Controller();
        View view = new View(controller);
        controller.setView(view);
        controller.selectJoystick(0);


        controller.setAxisAmount(2);
        controller.setButtonAmount(10);
        controller.setPovAmount(1);
    
        view.start();

        

        
    }
}