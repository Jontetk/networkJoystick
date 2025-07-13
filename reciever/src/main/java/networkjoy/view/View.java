package networkjoy.view;

import java.io.IOException;
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

    public void start() {
        System.out.println("Welcome to the reciever Type 'help' for available commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");

            if (parts.length == 0)
                continue;

            String command = parts[0].toLowerCase();
            try {
                switch (command.toLowerCase()) {
                    case "vjoy":
                        if (parts.length < 2) {
                            System.out.println("Usage: vjoy <vjoy id>");
                        } else {
                            controller.selectVjoy(Integer.parseInt(parts[1]));
                        }
                        break;

                    case "help":
                        printHelp();
                        break;
                    case "start":

                        System.out.println(
                                "Started recieving data\nType stop and press enter to stop and return to menu");
                        controller.recieveData();

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
        System.out.println("  vjoy <vjoy id>            - Selects Vjoy");
        System.out.println("  start                     - starts recieving");
        System.out.println(
                "  server <port>             - sets this reciever as a server with port and waits for client to connect");
        System.out.println(
                "  client <hostname> <port>  - sets this reciever as a client and connects to hostname and port");
        System.out.println("  exit                      - Exit the program");
    }
}
