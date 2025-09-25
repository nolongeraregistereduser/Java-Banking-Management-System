package model.exceptions;

public class TransactionInvalideException extends IllegalStateException {
    public TransactionInvalideException(String message) {
        super(message);
    }
}
