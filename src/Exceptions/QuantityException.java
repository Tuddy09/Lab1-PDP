package Exceptions;

public class QuantityException extends Exception {
    public QuantityException() {
        super("Quantity is not enough");
    }
}
