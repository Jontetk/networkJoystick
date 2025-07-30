package networkjoy.view;

public class Command {
    private String command;
    private String helpMessage;

    public Command(String command, String helpMessage) {
        this.command = command;
        this.helpMessage = helpMessage;
    }

    public String getCommand() {
        return command;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

}
