package networkjoy.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.text.Keymap;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import networkjoy.controller.Controller;
import networkjoy.controller.OperationFailedException;

public class View {
    private final Controller controller;
    private final Terminal terminal;
    private final LineReader lineReader;
    private final PrintWriter printWriter;
    private final ArrayList<Command> commands;

    public View(Controller controller) throws IOException {
        this.controller = controller;


        commands = new ArrayList<Command>(14);
        commands.add(new Command("listjoy", "Lists all available joysticks"));
        commands.add(new Command("setaxis", "Sets the number of axis"));
        commands.add(new Command("setbuttons", "Sets the number of buttons"));
        commands.add(new Command("setpovs", "Sets the number of povs"));
        commands.add(new Command("selectjoy", "Select a joystick to read from"));
        commands.add(new Command("start", "Starts sending"));
        commands.add(new Command("server", "Sets this sender as a server with port and waits for client to connect"));
        commands.add(new Command("client", "Sets this sender as a client and connects to hostname and port"));
        commands.add(new Command("bind", "Binds the selected type to vjoy ids"));
        commands.add(new Command("exit", "Exit the program"));
        commands.add(new Command("save", "Saves the current bindings"));
        commands.add(new Command("load", "Loads the bindings from Bindings.txt"));
        commands.add(new Command("stopconnection",
                "Stops the current network connection. The connection needs to be setup again"));
        String[] commandArray = new String[commands.size()];
        for (int i =0 ;i<commands.size();i++) {
            commandArray[i] = commands.get(i).getCommand();
        }
        Completer commandCompleter = new StringsCompleter(Arrays.asList(commandArray));
        this.terminal = TerminalBuilder.builder().system(true).build();
        this.lineReader = LineReaderBuilder.builder().terminal(this.terminal).completer(commandCompleter).build();
        this.printWriter = this.terminal.writer();
        lineReader.getWidgets().put("stop", this::stop);
        lineReader.getKeyMaps().get(LineReader.MAIN).bind(new Reference("stop"), KeyMap.ctrl('b'));

    }

    public void start() {
        printWriter.println("Welcome to the sender Type 'help' for available commands.");
        printWriter.flush();

        while (true) {

            String input = lineReader.readLine(">").trim();
            String[] parts = input.split("\\s+");

            if (parts.length == 0)
                continue;

            String command = parts[0].toLowerCase();
            try {
                switch (command.toLowerCase()) {
                    case "listjoy":
                        Map<String, Integer> connectedJoysticks = controller.getAllJoysticks();

                        for (String key : connectedJoysticks.keySet()) {
                            printWriter.println(key + " : " + connectedJoysticks.get(key));
                            printWriter.flush();
                        }

                        break;
                    case "setaxis":
                        if (parts.length < 2) {
                            printWriter.println("Usage: setaxis <axis amount>");
                            printWriter.flush();
                        } else {

                            controller.setAxisAmount(Integer.parseInt(parts[1]));

                        }
                        break;
                    case "setbuttons":
                        if (parts.length < 2) {
                            printWriter.println("Usage: setbuttons <button amount>");
                            printWriter.flush();
                        } else {

                            controller.setButtonAmount(Integer.parseInt(parts[1]));

                        }
                        break;
                    case "setpovs":
                        if (parts.length < 2) {
                            printWriter.println("Usage: setpovs <pov amount>");
                            printWriter.flush();
                        } else {

                            controller.setPovAmount(Integer.parseInt(parts[1]));

                        }
                        break;

                    case "selectjoy":
                        if (parts.length < 2) {
                            printWriter.println("Usage: selectjoy <joy id>");
                            printWriter.flush();
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
                                .print("Started sending data\nPress Ctrl+b to stop and return to menu");

                        controller.sendData();

                        break;
                    case "server":
                        if (parts.length < 2) {
                            printWriter.println("Usage: server <port>");
                            printWriter.flush();
                        } else {

                            controller.setupNetwork(true, "", Integer.parseInt(parts[1]));

                        }
                        break;

                    case "client":
                        if (parts.length < 3) {
                            printWriter.println("Usage: client <hostname> <port>");
                            printWriter.flush();
                        } else {

                            controller.setupNetwork(false, parts[1], Integer.parseInt(parts[2]));

                        }
                        break;

                    case "bind":
                        if (parts.length < 3) {
                            printWriter.println("Usage: bind <type> [ids]");
                            printWriter.flush();
                        } else {

                            switch (parts[1].toLowerCase()) {
                                case "button":
                                    for (int i = 2; i < parts.length; i++) {
                                        printWriter.print(
                                                "Binding button " + parts[i] + " press Ctrl+b to continue to next button");
                                        printWriter.flush();
                                        controller.bindButtons(Integer.parseInt(parts[i]));
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            System.err.println("Error occured, exiting");
                                            System.exit(1);

                                        }
                                    }
                                    break;
                                case "axis":
                                    for (int i = 2; i < parts.length; i++) {
                                        printWriter.print(
                                                "Binding axis " + parts[i] + " press Ctrl+b to continue to next axis");
                                        printWriter.flush();
                                        controller.bindAxis(Integer.parseInt(parts[i]));

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            System.err.println("Error occured, exiting");
                                            System.exit(1);
                                        }

                                    }
                                    break;
                                case "pov":
                                    for (int i = 2; i < parts.length; i++) {
                                        printWriter.print(
                                                "Binding pov " + parts[i] + " press Ctrl+b to continue to next pov");
                                        printWriter.flush();
                                        controller.bindPovs(Integer.parseInt(parts[i]));

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            System.err.println("Error occured, exiting");
                                            System.exit(1);
                                        }

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
                        printWriter.println("Unknown command. Type 'help' for options.");
                        printWriter.flush();
                        break;
                }
            } catch (OperationFailedException e) {
                printWriter.println(e.getMessage());
                printWriter.flush();
            } catch (java.lang.NumberFormatException e) {
                printWriter.println("Incorrect format");
                printWriter.flush();
            }
        }
    }

    public void takeInput() {

        try {
            lineReader.readLine();
        } catch (UserInterruptException e) {

        }

    }

    public boolean stop() {
        return stop(null);
    }

    public boolean stop(Thread thread) {
        controller.stop();
        while (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        return true;
    }

    private void printHelp() {
        printWriter.println("Available commands:");
        printWriter.flush();
        for (Command command : commands) {
            StringBuilder builder = new StringBuilder();
            builder.append(command.getCommand());
            builder.repeat(" ", 20 - command.getCommand().length());
            builder.append("-");
            builder.append(command.getHelpMessage());

            printWriter.println(builder.toString());
            printWriter.flush();
        }
    }
}
