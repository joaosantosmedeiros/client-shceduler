package god.joaopedro.client_scheduler.config;

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handle (IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
