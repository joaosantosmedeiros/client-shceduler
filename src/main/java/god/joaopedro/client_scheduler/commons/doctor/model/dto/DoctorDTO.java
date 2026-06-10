package god.joaopedro.client_scheduler.commons.doctor.model.dto;

import jakarta.validation.constraints.*;

import java.util.Date;

public record DoctorDTO(
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(min = 11, max = 11)
        String cpf,

        @NotBlank
        @Size(max = 15)
        @Pattern(regexp = "(\\d{1,12}/[A-Z]{2}$)", message = "deve conter de 1-12 dígitos, seguido de / + sigla do estado")
        String crm,

        @Pattern(regexp = "^(\\d{8}|\\d{9}|\\d{11})$", message = "deve conter apenas números, com 8, 9 ou 11 dígitos")
        String phone,

        @NotNull
        @Past
        Date birthDate
) {
}
