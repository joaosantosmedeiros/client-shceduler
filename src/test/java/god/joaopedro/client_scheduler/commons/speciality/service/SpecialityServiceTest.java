package god.joaopedro.client_scheduler.commons.speciality.service;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.repository.SpecialityRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import org.junit.jupiter.api.Nested;
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

    private Speciality makeSpeciality(UUID id, String name, Boolean isActive) {
        Speciality s = new Speciality(id);
        s.setName(name);
        s.setIsActive(isActive);
        return s;
    }

    @Nested
    class OnCreate{

        @Test public void itShouldThrowIfDtoIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.create(null));
        }

        @Test public void itShouldThrowIfNameIsAlreadyInUse() {
            when(repository.findByName(dto.name())).thenReturn(Optional.of(new Speciality(dto)));

            Exception e = assertThrows(InvalidFieldException.class, () -> service.create(dto));
            assertTrue(e.getMessage().contains(Constants.IN_USE));
        }

        @Test public void itShouldNotThrowIfValidDtoIsPassed() {
            when(repository.findByName(dto.name())).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> service.create(dto));
        }
    }

    @Nested
    class OnUpdate {

        @Test public void itShouldThrowIfDtoIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
        }

        @Test public void itShouldThrowIfSpecialityIsNotFound() {
            when(repository.findById(any())).thenReturn(Optional.empty());

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(UUID.randomUUID(), dto));
            assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
        }

        @Test public void itShouldThrowIfNameIsInUse() {
            when(repository.findByName(any())).thenReturn(Optional.of(makeSpeciality(UUID.randomUUID(), "name", Boolean.TRUE)));
            when(repository.findById(any())).thenReturn(Optional.of(makeSpeciality(UUID.randomUUID(), "another_name", Boolean.TRUE)));

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(UUID.randomUUID(), dto));
            assertTrue(e.getMessage().contains(Constants.IN_USE));
        }

        @Test public void itShouldNotCallRepositoryIfNoChangesAreMade() {
            when(repository.findById(any())).thenReturn(Optional.of(new Speciality((dto))));

            assertDoesNotThrow(() -> service.update(UUID.randomUUID(), dto));

            verify(repository, times(0)).save(any());
        }

        @Test public void itShouldCallRepositoryIfChangesAreMade() {
            Speciality speciality = new Speciality();
            speciality.setName("another_name");
            when(repository.findById(any())).thenReturn(Optional.of(speciality));

            assertDoesNotThrow(() -> service.update(UUID.randomUUID(), dto));

            verify(repository, times(1)).save(any());
        }
    }

    @Nested
    class OnDelete{

        @Test public void itShouldThrowIfNoSpecialityIsFound() {
            when(repository.findById(any())).thenReturn(Optional.empty());

            assertThrows(InvalidFieldException.class, () -> service.delete(UUID.randomUUID()));
        }

        @Test public void itShouldNotCallSaveIfNoChangesAreMade() {
            UUID id = UUID.randomUUID();
            when(repository.findById(any())).thenReturn(Optional.of(makeSpeciality(id, "name", Boolean.FALSE)));

            assertDoesNotThrow(() -> service.delete(id));

            verify(repository, times(0)).save(any());

        }

        @Test public void itShouldCallSaveIfChangesAreMade() {
            UUID id = UUID.randomUUID();
            when(repository.findById(any())).thenReturn(Optional.of(makeSpeciality(id, "name", Boolean.TRUE)));

            assertDoesNotThrow(() -> service.delete(id));

            verify(repository).save(any());
        }
    }
}