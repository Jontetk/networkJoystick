package networkjoy.controller;

import java.io.IOException;

import networkjoy.view.View;
import networkjoy.integration.Network;
import networkjoy.integration.Vjoy;
import networkjoy.util.RecievedJoyData;

public class Controller {
    private Vjoy vjoy = null;
    private Network network;
    private boolean running;
    private View view;

    public Controller() {
        this.running = false;
    }

    public void selectVjoy(int vjoyId) {
        if (this.vjoy != null) {
            this.vjoy.releaseOwn();
        }
        this.vjoy = new Vjoy(vjoyId);
        if (!vjoy.takeOwn()) {
            this.vjoy = null;
            throw new OperationFailedException(
                    "Couldn't take ownership, Make sure the vjoy is free or choose another vjoy");
        } else {
            vjoy.reset();
        }

    }

    public void setView(View view) {
        this.view = view;
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
        public void stopConnection() {
        try {
            network.stopConnection();
        } catch (IOException e) {
            throw new OperationFailedException("Couldn't stop connection: "+e);
        }
        
    }
    public void stop() {
        running = false;
    }
    public void recieveData() {
        if (vjoy == null) {
            throw new OperationFailedException("No Vjoy device selected please select a vjoy deveice");
        }
        if (network == null) {
            throw new OperationFailedException("No network setup, please set up as client or server");
        }
        running = true;
        while (running) {
            view.takeInput();
            try {
                RecievedJoyData recieved = network.recieveData();

                for (byte i = 1; i <= recieved.getButtonDatas().length; i++) {
                    vjoy.setButton(recieved.getButtonDatas()[i - 1], i);
                }
                for (int i = 1; i <= recieved.getAxisDatas().length; i++) {
                    vjoy.setAxis(recieved.getAxisDatas()[i - 1], Vjoy.HID_USAGES.get(i - 1));
                }
                for (byte i = 1; i <= recieved.getPovDatas().length; i++) {
                    vjoy.setPov(35999 / 8 * (recieved.getPovDatas()[i - 1] - 1), i, true);
                }
            } catch (IOException e) {
                network = null;
                throw new OperationFailedException("Couldn't recieve data: " + e);
            }
        }
    }
}
