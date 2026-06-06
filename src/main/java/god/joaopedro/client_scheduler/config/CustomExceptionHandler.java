package god.joaopedro.client_scheduler.config;

import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.ValidationErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle (MethodArgumentNotValidException exception) {
        ValidationErrorMessage errorMessage = new ValidationErrorMessage();
        for(FieldError err : exception.getFieldErrors())
            errorMessage.addError(err.getField(), err.getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Object> handle (InvalidFieldException exception) {
        ValidationErrorMessage errorMessage = new ValidationErrorMessage();
        errorMessage.addError(exception.getField(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle (Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
