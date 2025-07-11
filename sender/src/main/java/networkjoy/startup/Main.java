package networkjoy.startup;


import networkjoy.controller.Controller;
import networkjoy.integration.Joystick;
import networkjoy.view.View;

public class Main {
    public static void main(String[] args) throws Exception {
        Joystick.joystickInit();


        Controller controller = new Controller();
        View view = new View(controller);
        controller.setView(view);


        view.start();

    }
}