package god.joaopedro.client_scheduler.commons.speciality.service;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.repository.SpecialityRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecialityServiceTest {

    @Mock private SpecialityRepository repository;

    @InjectMocks private SpecialityService service;

    private final SpecialityDTO dto = new SpecialityDTO("Name");

    @Test public void itShouldThrowIfDtoIsNullOnCreate() {
        assertThrows(IllegalArgumentException.class, () -> service.createSpeciality(null));
    }

    @Test public void itShouldThrowIfNameIsAlreadyInUseOnCreate() {
        when(repository.findByName(dto.name())).thenReturn(Optional.of(new Speciality(dto)));

        Exception e = assertThrows(InvalidFieldException.class, () -> service.createSpeciality(dto));
        assertTrue(e.getMessage().contains(Constants.IN_USE));
    }

    @Test public void itShouldNotThrowIfValidDtoIsPassedOnCreate() {
        when(repository.findByName(dto.name())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.createSpeciality(dto));
    }
}