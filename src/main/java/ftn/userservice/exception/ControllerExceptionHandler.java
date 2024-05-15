package ftn.userservice.exception;

import ftn.userservice.exception.exceptions.BadRequestException;
import ftn.userservice.exception.exceptions.ForbiddenException;
import ftn.userservice.exception.exceptions.NotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidation(ValidationException exception) {
        return ResponseEntity.status(400).body(new ExceptionMessage("Invalid form: " + exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException exception) {
        HttpStatus status = getResponseStatus(exception.getClass());
        return ResponseEntity.status(status).body(new ExceptionMessage(exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException exception) {
        HttpStatus status = getResponseStatus(exception.getClass());
        return ResponseEntity.status(status).body(new ExceptionMessage("Forbidden: " + exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleNotFound(BadRequestException exception) {
        HttpStatus status = getResponseStatus(exception.getClass());
        return ResponseEntity.status(status).body(new ExceptionMessage(exception.getMessage()));
    }

    private HttpStatus getResponseStatus(Class<? extends Throwable> exceptionClass) {
        ResponseStatus responseStatus = exceptionClass.getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
