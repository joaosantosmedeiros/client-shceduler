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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialityServiceTest {

    @Mock private SpecialityRepository repository;

    @InjectMocks private SpecialityService service;

    private final SpecialityDTO dto = new SpecialityDTO("Name");

    private Speciality makeSpeciality(UUID id, String name) {
        Speciality s = new Speciality(id);
        s.setName(name);
        return s;
    }

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

    @Test public void itShouldThrowIfDtoIsNullOnUpdate() {
        assertThrows(IllegalArgumentException.class, () -> service.updateSpeciality(UUID.randomUUID(), null));
    }

    @Test public void itShouldThrowIfSpecialityIsNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        Exception e = assertThrows(InvalidFieldException.class, () -> service.updateSpeciality(UUID.randomUUID(), dto));
        assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
    }

    @Test public void itShouldThrowIfNameIsInUseOnUpdate() {
        when(repository.findByName(any())).thenReturn(Optional.of(makeSpeciality(UUID.randomUUID(), "name")));
        when(repository.findById(any())).thenReturn(Optional.of(makeSpeciality(UUID.randomUUID(), "another_name")));

        Exception e = assertThrows(InvalidFieldException.class, () -> service.updateSpeciality(UUID.randomUUID(), dto));
        assertTrue(e.getMessage().contains(Constants.IN_USE));
    }

    @Test public void itShouldNotCallRepositoryIfNoChangesAreMade() {
        when(repository.findById(any())).thenReturn(Optional.of(new Speciality((dto))));

        assertDoesNotThrow(() -> service.updateSpeciality(UUID.randomUUID(), dto));

        verify(repository, times(0)).save(any());
    }

    @Test public void itShouldCallRepositoryIfChangesAreMade() {
        Speciality speciality = new Speciality();
        speciality.setName("another_name");
        when(repository.findById(any())).thenReturn(Optional.of(speciality));

        assertDoesNotThrow(() -> service.updateSpeciality(UUID.randomUUID(), dto));

        verify(repository, times(1)).save(any());
    }
}