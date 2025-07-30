package networkjoy.view;

import java.util.List;

public class Command {
    private String command;
    private String helpMessage;
    private List<Command> subCommands;

    public Command(String command, String helpMessage) {
        this(command, helpMessage, null);
    }

    public Command(String command, String helpMessage, List<Command> subCommands) {
        this.command = command;
        this.helpMessage = helpMessage;
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

}
