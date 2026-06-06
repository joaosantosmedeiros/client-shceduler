package god.joaopedro.client_scheduler.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class ValidationErrorMessage {

    private List<Map<String, String>> errors = new ArrayList<>();

    public void addError(String field, String message) {
        if(field != null && message != null) {
            errors.add(Map.of("field", field, "message", message));
        }
    }
}
