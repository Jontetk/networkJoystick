package networkjoy.controller;

public class OperationFailedException extends RuntimeException{
    public OperationFailedException(String msg) {
        super(msg);
    }
}
