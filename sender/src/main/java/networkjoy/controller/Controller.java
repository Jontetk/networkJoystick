package networkjoy.controller;

import java.io.IOException;
import java.util.Map;

import networkjoy.integration.Network;
import networkjoy.model.JoystickHandler;
import networkjoy.util.ConvertedJoystickInputs;
import networkjoy.view.View;

public class Controller {

    private int[] assignedButtons;
    private int[] assignedAxis;
    private int[] assignedPovs;
    private JoystickHandler joystickHandler;
    private boolean running;
    private View view;
    private Network network;

    public Controller() {
        this.running = false;

    }

    public void selectJoystick(int joyId) {
        this.joystickHandler = new JoystickHandler(joyId, assignedButtons, assignedAxis, assignedPovs);
    }

    public Map<String, Integer> getAllJoysticks() {
        return JoystickHandler.getAllJoysticks();
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean getRunning() {
        return running;
    }

    public void setupNetwork(boolean server, String hostname, int port) {

        try {
            if (network != null) {
                network.stopConnection();
            }
            this.network = new Network(server, hostname, port);
        } catch (IOException e) {
            throw new OperationFailedException("Couldn't setup network connection: " + e);
        }

    }

    public void sendData() {
        if (joystickHandler == null) {
            throw new OperationFailedException("No joystick selected please select a joystick");
        }
        if (network == null) {
            throw new OperationFailedException("No network setup, please set up as client or server");
        }
        try {
            running = true;
            while (running) {
                view.takeInput();
                ConvertedJoystickInputs conv = joystickHandler.convertJoysitckInputs();
                network.sendJoyData(conv.getButtonData(), conv.getAxisData(), conv.getPovData());
            }

        } catch (IOException e) {
            running = false;
            throw new OperationFailedException("Couldn't send data: " + e);
            

        }
        running = false;

    }

    public void setButtonAmount(int buttonAmount) {
        if (joystickHandler == null) {
            throw new OperationFailedException("Select a joystick first");
        }
        this.assignedButtons = new int[buttonAmount];
        joystickHandler.setAssignedButtons(this.assignedButtons);
    }

    public void setAxisAmount(int axisAmount) {
        if (joystickHandler == null) {
            throw new OperationFailedException("Select a joystick first");
        }
        this.assignedAxis = new int[axisAmount];
        joystickHandler.setAssignedAxis(this.assignedAxis);
    }

    public void setPovAmount(int povAmount) {
        if (joystickHandler == null) {
            throw new OperationFailedException("Select a joystick first");
        }
        this.assignedPovs = new int[povAmount];
        joystickHandler.setAssignedPovs(this.assignedPovs);
    }

    public void bindButtons(int bId) {
        running = true;
        try {
            while (!joystickHandler.bindButtons(bId) && running) {
                view.takeInput();
            }
        } catch (java.lang.NullPointerException e) {
            running = false;
            throw new OperationFailedException("No buttons on joystick or joystick not connected");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            running = false;
            throw new OperationFailedException("To few buttons set, set a higher amount of buttons");
        }
    }

    public void bindAxis(int axId) {
        running = true;
        try {
            while (!joystickHandler.bindAxis(axId) && running) {
                view.takeInput();
            }
        } catch (java.lang.NullPointerException e) {
            running = false;
            throw new OperationFailedException("No axis on joystick or joystick not connected");
        }catch (java.lang.ArrayIndexOutOfBoundsException e) {
            running = false;
            throw new OperationFailedException("To few axis set, set a higher amount of axis");
        }
        running = false;
    }

    public void bindPovs(int povId) {
        running = true;
        try {
            while (!joystickHandler.bindPovs(povId) && running) {
                view.takeInput();
            }
        } catch (java.lang.NullPointerException e) {
            running = false;
            throw new OperationFailedException("No povs on joystick or joystick not connected");
        }catch (java.lang.ArrayIndexOutOfBoundsException e) {
            running = false;
            throw new OperationFailedException("To few povs set, set a higher amount of povs");
        }
        running = false;
    }

    public void stop() {
        running = false;
    }

}
