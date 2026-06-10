package god.joaopedro.client_scheduler.commons.patient.service;

import god.joaopedro.client_scheduler.commons.patient.model.Patient;
import god.joaopedro.client_scheduler.commons.patient.model.dto.PatientDTO;
import god.joaopedro.client_scheduler.commons.patient.repository.PatientRepository;
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
class PatientServiceTest {

    @Mock private PatientRepository repository;

    @InjectMocks private PatientService service;

    private final UUID id = UUID.randomUUID();
    private String cpf = "70431492077";

    private PatientDTO createDTO() {
        return new PatientDTO(null, this.cpf, null, null);
    }

    @Nested
    class OnCreate{

        @Test public void itShouldThrowIfNullDtoIsPassed() {
            assertThrows(IllegalArgumentException.class, () -> service.create(null));
        }

        @Test public void itShouldThrowIfInvalidCPFIsPassed() {
            cpf = "70431492078";

            Exception e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));

            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT));
        }

        @Test public void itShouldThrowIfUsedCPFIsPassed() {
            when(repository.findByCpf(cpf)).thenReturn(Optional.of(new Patient()));

            Exception e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));

            assertTrue(e.getMessage().contains(Constants.IN_USE));
        }

        @Test public void itShouldCreateSuccessfully() {
            assertDoesNotThrow(() -> {
                service.create(createDTO());
            });
        }
    }

    @Nested
    class OnUpdate{

        @Test public void itShouldThrowIfIdIsInvalid() {
            when(repository.findById(id)).thenReturn(Optional.empty());

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
        }

        @Test public void itShouldThrowIfPassedDTOIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.update(id, null));
        }

        @Test public void itShouldThrowIfPatientIsNotFound() {
            when(repository.findById(any())).thenReturn(Optional.empty());

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
        }

        @Test public void itShouldThrowIfCpfIsInvalid() {
            when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
            cpf = "12345678910123";

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT));
        }

        @Test public void itShouldThrowIfCpfIsInUse() {
            when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
            when(repository.findByCpf(cpf)).thenReturn(Optional.of(new Patient(UUID.randomUUID())));

            Exception e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE));
        }

        @Test public void itShouldCallRepositorySaveOnSuccess() {
            when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
            when(repository.findByCpf(cpf)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> {
                service.update(id, createDTO());
                verify(repository).save(any());
            });
        }
    }

    @Nested
    class OnDelete{

        @Test public void itShouldThrowIfPatientIsNotFound() {
            assertThrows(InvalidFieldException.class, () -> service.delete(id));
        }

        @Test public void itShouldNotUpdateIfPatientIsAlreadyInactive() {
            Patient p = new Patient();
            p.setIsActive(Boolean.FALSE);
            when(repository.findById(any())).thenReturn(Optional.of(p));

            service.delete(id);

            verify(repository, times(0)).save(any());
        }

        @Test public void itShouldUpdateIfPatientIsActive() {
            Patient p = new Patient();
            p.setIsActive(Boolean.TRUE);
            when(repository.findById(any())).thenReturn(Optional.of(p));

            service.delete(id);

            verify(repository).save(any());
        }
    }

    @Nested
    class OnActivate{

        @Test public void itShouldThrowIfPatientIsNotFound() {
            assertThrows(InvalidFieldException.class, () -> service.activate(id));
        }

        @Test public void itShouldNotUpdateIfPatientIsAlreadyActive() {
            Patient p = new Patient();
            p.setIsActive(Boolean.TRUE);
            when(repository.findById(any())).thenReturn(Optional.of(p));

            service.activate(id);

            verify(repository, times(0)).save(any());
        }

        @Test public void itShouldUpdateIfPatientIsInactive() {
            Patient p = new Patient();
            p.setIsActive(Boolean.FALSE);
            when(repository.findById(any())).thenReturn(Optional.of(p));

            service.activate(id);

            verify(repository).save(any());
        }
    }
}