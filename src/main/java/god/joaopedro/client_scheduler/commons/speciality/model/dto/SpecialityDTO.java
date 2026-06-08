package god.joaopedro.client_scheduler.commons.speciality.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpecialityDTO(
        @NotBlank
        @Size(max = 50)
        String name
) {}
