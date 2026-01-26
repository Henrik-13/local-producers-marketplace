package edu.bbte.localproducersmarketplace.exception;

public class ImageRepositoryException extends RuntimeException {
    public ImageRepositoryException(String message) {
        super(message);
    }
    public ImageRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
