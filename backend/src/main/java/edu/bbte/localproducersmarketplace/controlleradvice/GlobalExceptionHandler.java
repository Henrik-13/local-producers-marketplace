package edu.bbte.localproducersmarketplace.controlleradvice;

import edu.bbte.localproducersmarketplace.dto.ErrorResponse;
import edu.bbte.localproducersmarketplace.exception.CartNotFoundException;
import edu.bbte.localproducersmarketplace.exception.OrderFinalizedException;
import edu.bbte.localproducersmarketplace.exception.OrderNotFoundException;
import edu.bbte.localproducersmarketplace.exception.ProductNotFoundException;
import edu.bbte.localproducersmarketplace.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;
import tools.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Stream<String> handleConstraintViolation(ConstraintViolationException e) {
        log.debug("ConstraintViolationException occurred", e);
        return e.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Stream<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException occurred", e);
        return e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleOrderNotFound(OrderNotFoundException ex) {
        log.error("OrderNotFoundException: ", ex);
        return new ErrorResponse("Order not found", ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        log.error("Product not found: ", ex);
        return new ErrorResponse("Product not found", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        log.error("UserNotFoundException: ", ex);
        return new ErrorResponse("User not found", ex.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleCartNotFound(CartNotFoundException ex) {
        log.error("CartNotFoundException: ", ex);
        return new ErrorResponse("Cart not found", ex.getMessage());
    }

    @ExceptionHandler(OrderFinalizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleOrderFinalized(OrderFinalizedException ex) {
        log.error("OrderFinalizedException: ", ex);
        return new ErrorResponse("Order cannot be modified", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.debug("HttpMessageNotReadableException occurred", ex);

        if (ex.getCause() instanceof InvalidFormatException invalidEx) {
            String targetType = invalidEx.getTargetType().getSimpleName();
            String value = invalidEx.getValue().toString();
            String msg = "Cannot deserialize value '" + value + "' to type " + targetType;
            return new ErrorResponse("Invalid value", msg);
        }

        return new ErrorResponse("Malformed JSON", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleOtherExceptions(Exception ex) {
        log.error("Unhandled exception: ", ex);
        return new ErrorResponse("Internal server error", ex.getMessage());
    }
}

