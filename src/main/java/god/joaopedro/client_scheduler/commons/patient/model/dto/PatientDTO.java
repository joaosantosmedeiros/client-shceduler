package god.joaopedro.client_scheduler.commons.patient.model.dto;

import jakarta.validation.constraints.*;

import java.util.Date;

public record PatientDTO(
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(min = 11, max = 11)
        String cpf,

        @Pattern(regexp = "^(\\d{8}|\\d{9}|\\d{11})$", message = "deve conter apenas números, com 8, 9 ou 11 dígitos")
        String phone,

        @NotNull
        @Past
        Date birthDate
) {
}
