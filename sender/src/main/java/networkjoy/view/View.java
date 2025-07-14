package networkjoy.view;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import networkjoy.controller.Controller;
import networkjoy.controller.OperationFailedException;

public class View {
    private final Controller controller;
    private final Scanner scanner;

    public View(Controller controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() throws InterruptedException {
        System.out.println("Welcome to Type 'help' for available commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");

            if (parts.length == 0)
                continue;

            String command = parts[0].toLowerCase();
            try {
                switch (command.toLowerCase()) {
                    case "listjoy":
                        Map<String, Integer> connectedJoysticks = controller.getAllJoysticks();

                        for (String key : connectedJoysticks.keySet()) {
                            System.out.println(key + " : " + connectedJoysticks.get(key));
                        }

                        break;
                    case "setaxis":
                        if (parts.length < 2) {
                            System.out.println("Usage: setaxis <axis amount>");
                        } else {

                            controller.setAxisAmount(Integer.parseInt(parts[1]));

                        }
                        break;
                    case "setbuttons":
                        if (parts.length < 2) {
                            System.out.println("Usage: setbuttons <button amount>");
                        } else {

                            controller.setButtonAmount(Integer.parseInt(parts[1]));

                        }
                        break;
                    case "setpovs":
                        if (parts.length < 2) {
                            System.out.println("Usage: setpovs <pov amount>");
                        } else {

                            controller.setPovAmount(Integer.parseInt(parts[1]));

                        }
                        break;

                    case "selectjoy":
                        if (parts.length < 2) {
                            System.out.println("Usage: selectjoy <joy id>");
                        } else {

                            controller.selectJoystick(Integer.parseInt(parts[1]));

                        }
                        break;
                    case "save":
                        controller.saveBindings();
                        break;
                    case "load":
                        controller.loadBindings();
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "stopconnection":
                        controller.stopConnection();
                        break;

                    case "start":
                        System.out
                                .println("Started sending data\nType stop and press enter to stop and return to menu");

                        controller.sendData();
                        break;
                    case "server":
                        if (parts.length < 2) {
                            System.out.println("Usage: server <port>");
                        } else {

                            controller.setupNetwork(true, "", Integer.parseInt(parts[1]));

                        }
                        break;

                    case "client":
                        if (parts.length < 3) {
                            System.out.println("Usage: client <hostname> <port>");
                        } else {

                            controller.setupNetwork(false, parts[1], Integer.parseInt(parts[2]));

                        }
                        break;

                    case "bind":
                        if (parts.length < 3) {
                            System.out.println("Usage: bind <type> [ids]");
                        } else {

                            switch (parts[1].toLowerCase()) {
                                case "button":
                                    for (int i = 2; i < parts.length; i++) {
                                        System.out.println(
                                                "Binding button " + parts[i] + " type stop to continue to next button");
                                        controller.bindButtons(Integer.parseInt(parts[i]));
                                        Thread.sleep(500);
                                    }
                                    break;
                                case "axis":
                                    for (int i = 2; i < parts.length; i++) {
                                        System.out.println(
                                                "Binding axis " + parts[i] + " type stop to continue to next axis");
                                        controller.bindAxis(Integer.parseInt(parts[i]));
                                        Thread.sleep(500);

                                    }
                                    break;
                                case "pov":
                                    for (int i = 2; i < parts.length; i++) {
                                        System.out.println(
                                                "Binding pov " + parts[i] + " type stop to continue to next pov");
                                        controller.bindPovs(Integer.parseInt(parts[i]));
                                        Thread.sleep(500);

                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Unknown command. Type 'help' for options.");
                        break;
                }
            } catch (OperationFailedException e) {
                System.out.println(e.getMessage());
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Incorrect format");
            }
        }
    }

    public void takeInput() {
        String input = "";
        try {
            if (System.in.available() > 0) {
                input = scanner.nextLine().trim();
            }

            if (input.equalsIgnoreCase("stop")) {
                controller.stop();
            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  listjoy                       - Lists all available joysticks");
        System.out.println("  setaxis <axis amount>         - Sets the number of axis");
        System.out.println("  setbuttons <button amount>    - Sets the number of buttons");
        System.out.println("  setpovs <pov amount>          - Sets the number of povs");
        System.out.println("  selectjoy                     - Select a joystick to read from");
        System.out.println("  start                         - starts sending");
        System.out.println(
                "  server <port>                 - sets this sender as a server with port and waits for client to connect");
        System.out.println(
                "  client <hostname> <port>      - sets this sender as a client and connects to hostname and port");
        System.out.println("  bind <type> [ids]             - Binds the selected type to vjoy ids");
        System.out.println("  exit                          - Exit the program");
        System.out.println("  save                          - Saves the current bindings");
        System.out.println("  load                          - Loads the bindings from Bindings.txt");
        System.out.println(
                "  stopconnection                - Stops the current network connection. The connection needs to be setup again");
    }
}
