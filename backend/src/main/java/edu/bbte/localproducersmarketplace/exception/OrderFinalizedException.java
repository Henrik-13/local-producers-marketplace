package edu.bbte.localproducersmarketplace.exception;

public class OrderFinalizedException extends RuntimeException {
    public OrderFinalizedException(String message) {
        super(message);
    }
}

