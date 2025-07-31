package networkjoy.view;

import java.util.List;

public class Command {
    private String command;
    private String commandArg;
    private String helpMessage;
    private List<Command> subCommands;

    public Command(String command, String helpMessage) {
        this(command, null, helpMessage, null);
    }

    public Command(String command, String commandArg, String helpMessage) {
        this(command,commandArg , helpMessage, null);
    }

    public Command(String command, String commandArg, String helpMessage, List<Command> subCommands) {
        this.command = command;
        this.helpMessage = helpMessage;
        this.commandArg = commandArg;
        this.subCommands = subCommands;
    }

    public String getCommand() {
        return command;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }

    public String getCommandArg() {
        return commandArg;
    }

}
