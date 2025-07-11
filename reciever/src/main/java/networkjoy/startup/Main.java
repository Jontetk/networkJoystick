package networkjoy.startup;

import networkjoy.controller.Controller;

import networkjoy.view.View;

public class Main {
    public static void main(String[] args)  {
        Controller controller = new Controller();
        View view = new View(controller);
        controller.setView(view);

        view.start();



       

    }
}