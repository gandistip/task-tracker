package exceptions;

public class InputException extends Exception {
    public InputException() {
    }

    public InputException(final String message) {
        super(message);
    }
}