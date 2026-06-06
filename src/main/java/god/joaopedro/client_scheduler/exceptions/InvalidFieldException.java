package god.joaopedro.client_scheduler.exceptions;

import lombok.Getter;

@Getter
public class InvalidFieldException extends RuntimeException{
    private final String field;
    private final String message;

    public InvalidFieldException(String field, String message) {
        this.field = field;
        this.message = message;
        super(message);
    }
}
