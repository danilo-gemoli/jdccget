package jdcc.events.messages;

public abstract class ErrorMessage extends Message {

    public Exception exception;

    public ErrorMessage() { }

    public ErrorMessage(Exception exception) {
        this.exception = exception;
    }

}
