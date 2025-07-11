package networkjoy.view;

import java.io.IOException;
import java.util.Scanner;
import networkjoy.controller.Controller;

public class View {
    private final Controller controller;
    private final Scanner scanner;

    public View(Controller controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Type 'help' for available commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");

            if (parts.length == 0)
                continue;

            String command = parts[0].toLowerCase();

            switch (command.toLowerCase()) {

                case "help":
                    printHelp();
                    break;
                case "start":
                    controller.sendData();
                    break;

                case "bind":
                    if (parts.length < 3) {
                        System.out.println("Usage: bind type id");
                    } else {

                        switch (parts[1].toLowerCase()) {
                            case "button":
                                controller.bindButtons(Integer.parseInt(parts[2]));
                                break;
                            case "axis":
                                controller.bindAxis(Integer.parseInt(parts[2]));
                                break;
                            case "pov":
                                controller.bindPovs(Integer.parseInt(parts[2]));
                                break;
                            default:
                                break;
                        }
                    }
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for options.");
                    break;
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
        System.out.println("  greet              - Print greeting");
        System.out.println("  add <a> <b>        - Add two numbers");
        System.out.println("  exit               - Exit the program");
    }
}
