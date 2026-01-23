package edu.bbte.localproducersmarketplace.exception;

public class ImageServiceException extends RuntimeException {
    public ImageServiceException(String message) {
        super(message);
    }
    public ImageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

