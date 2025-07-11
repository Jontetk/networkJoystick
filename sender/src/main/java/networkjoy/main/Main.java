package networkjoy.main;

import java.util.Map;

import networkjoy.controller.Controller;
import networkjoy.integration.Joystick;
import networkjoy.view.View;

public class Main {
    public static void main(String[] args) throws Exception {
        Joystick.joystickInit();
        Map<String, Integer> connectedJoysticks = Joystick.getAllJoy();
        System.out.println("HEJ");
        for (String key : connectedJoysticks.keySet()) {
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