package networkjoy.Controller;


import networkjoy.Integration.Network;
import networkjoy.Model.JoystickHandler;
import networkjoy.Model.JoystickHandler.ConvertedJoystickInputs;
import networkjoy.View.View;

public class Controller {

    private int[] assignedButtons;
    private int[] assignedAxis;
    private int[] assignedPovs;
    private JoystickHandler joystickHandler;
    private boolean running;
    private View view;
    
    public Controller() {
        this.running = false;

    }

    public void selectJoystick(int joyId){
        this.joystickHandler = new JoystickHandler(joyId,assignedButtons,assignedAxis,assignedPovs);
    }
    public void setView(View view) {
        this.view = view;
    }

    public boolean getRunning() {
        return running;
    }
    public void sendData(){
        try {
        Network network = new Network(true, null, 44801);
        while (true) {
            ConvertedJoystickInputs conv = joystickHandler.convertJoysitckInputs();
            network.sendJoyData(conv.buttonData, conv.axisData, conv.povData);
        }


        } catch (Exception e) {
            System.out.println(e);
        }



    }
    public void setButtonAmount(int buttonAmount) {
        this.assignedButtons = new int[buttonAmount];
        joystickHandler.setAssignedButtons(this.assignedButtons);
    }
    public void setAxisAmount(int axisAmount){
        this.assignedAxis = new int [axisAmount];
        joystickHandler.setAssignedAxis(this.assignedAxis);
    } 

    public void setPovAmount(int povAmount) {
        this.assignedPovs = new int[povAmount];
        joystickHandler.setAssignedPovs(this.assignedPovs);
    }

    public void bindButtons(int bId) {
        running=true;
        while(!joystickHandler.bindButtons(bId)&& running){view.takeInput();}
        running = false;
    }
    public void bindAxis(int axId){
        running=true;
        while(!joystickHandler.bindAxis(axId)&& running){view.takeInput();}
        running = false;
    }
    public void bindPovs(int povId){
        running=true;
        while (!joystickHandler.bindPovs(povId) && running) {
            view.takeInput();
        }
        running=false;
    }



    public void stop() {
        running = false;
    }

}
