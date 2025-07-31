package networkjoy.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jline.builtins.Completers.TreeCompleter;
import org.jline.keymap.KeyMap;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Reference;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.UserInterruptException;


import networkjoy.controller.Controller;
import networkjoy.controller.OperationFailedException;

public class View {
    private final Controller controller;
    private final Terminal terminal;
    private final LineReader lineReader;
    private volatile boolean running;
    private final PrintWriter printWriter;
    private final ArrayList<Command> commands;

    public View(Controller controller) throws IOException {
        this.controller = controller;

        commands = new ArrayList<Command>(6);

        commands.add(new Command("vjoy", "<vjoy id>", "Selects Vjoy"));
        commands.add(new Command("start", "starts recieving"));
        commands.add(new Command("server", "<port>",
                "sets this reciever as a server with port and waits for client to connect"));
        commands.add(new Command("client", "<hostname> <port>",
                "sets this reciever as a client and connects to hostname and port"));
        commands.add(new Command("exit", "Exit the program"));
        commands.add(new Command("stopconnection",
                "Stops the current network connection. The connection needs to be setup again"));

        Completer commandCompleter = new TreeCompleter(createNodesFromCommands(commands));
        this.terminal = TerminalBuilder.builder().system(true).build();
        this.lineReader = LineReaderBuilder.builder().terminal(this.terminal).completer(commandCompleter).build();
        this.printWriter = this.terminal.writer();
        lineReader.getWidgets().put("stop", this::stop);
        lineReader.getKeyMaps().get(LineReader.MAIN).bind(new Reference("stop"), KeyMap.ctrl('b'));

    }

    private List<TreeCompleter.Node> createNodesFromCommands(List<Command> commandsList) {
        List<TreeCompleter.Node> nodeList = new ArrayList<TreeCompleter.Node>();
        for (Command command : commandsList) {
            if (command.getSubCommands() == null) {
                nodeList.add(TreeCompleter.node(command.getCommand()));
            } else {
                ArrayList<Object> args = new ArrayList<Object>();
                args.add(command.getCommand());
                args.addAll(createNodesFromCommands(command.getSubCommands()));

                nodeList.add(TreeCompleter.node(args.toArray()));
            }

        }
        return nodeList;
    }

    public void start() {
        printWriter.println("Welcome to the reciever Type 'help' for available commands.");
        printWriter.flush();

        while (true) {
            String input = lineReader.readLine(">").trim();

            String[] parts = input.split("\\s+");

            if (parts.length == 0)
                continue;

            String command = parts[0].toLowerCase();
            try {
                switch (command.toLowerCase()) {
                    case "vjoy":
                        if (parts.length < 2) {
                            printWriter.println("Usage: vjoy <vjoy id>");
                            printWriter.flush();
                        } else {
                            controller.selectVjoy(Integer.parseInt(parts[1]));
                        }
                        break;

                    case "help":
                        printHelp();
                        break;
                    case "start":

                        printWriter.print(
                                "Started recieving data\nPress Ctrl+b to stop and return to menu");
                        printWriter.flush();
                        controller.recieveData();

                        break;
                    case "stopconnection":
                        controller.stopConnection();
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
        running = true;
        try {
            while(running) {
            lineReader.readLine();
            }
        } catch (UserInterruptException e) {

        }

    }

    public boolean stop() {
        return stop(null);
    }

    public boolean stop(Thread thread) {
        running = false;
        lineReader.getBuffer().write("\n");

        while (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        controller.stop();
        return true;
    }

    private void printHelp() {
        printWriter.println("Available commands:");
        printWriter.flush();
        int repeatTimes = 1;
        int commandLength;

        for (Command command : commands) {
            if (command.getCommandArg() != null) {
                commandLength = command.getCommand().length() + 1 + command.getCommandArg().length();
            } else {
                commandLength = command.getCommand().length();
            }
            while (repeatTimes - commandLength < 5) {
                repeatTimes += 1;
            }
        }
        for (Command command : commands) {
            StringBuilder builder = new StringBuilder();
            builder.append(command.getCommand());
            if (command.getCommandArg() != null) {
                builder.append(" ");
                builder.append(command.getCommandArg());
                commandLength = command.getCommand().length() + 1 + command.getCommandArg().length();
            } else {
                commandLength = command.getCommand().length();
            }
            for (int i = 0; i < repeatTimes - commandLength; i++) {
                builder.append(" ");
            }

            builder.append("-");
            builder.append(command.getHelpMessage());

            printWriter.println(builder.toString());
            printWriter.flush();
        }
    }
}
