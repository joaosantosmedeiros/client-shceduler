package god.joaopedro.client_scheduler.commons.patient.service;

import god.joaopedro.client_scheduler.commons.patient.model.Patient;
import god.joaopedro.client_scheduler.commons.patient.model.dto.PatientDTO;
import god.joaopedro.client_scheduler.commons.patient.repository.PatientRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import god.joaopedro.client_scheduler.utils.CpfValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private CpfValidator cpfValidator;
    @Mock private PatientRepository repository;

    @InjectMocks private PatientService service;

    private final UUID id = UUID.randomUUID();
    private String name = "J";
    private String cpf = "11339213451";
    private String phone = "12345678";
    private Date birthDate = new Date();

    private PatientDTO createDTO() {
        return new PatientDTO(this.name, this.cpf, this.phone, this.birthDate);
    }


    @Test public void itShouldThrowIfNullDtoIsPassedOnCreation() {
        assertThrows(IllegalArgumentException.class, () -> {
           service.createPatient(null);
        });
    }

    @Test public void itShouldThrowIfInvalidCPFIsPassedOnCreation() {
        this.cpf = "11339213452";

        Exception e = assertThrows(InvalidFieldException.class, () -> {
            service.createPatient(createDTO());
        });

        assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT));
    }

    @Test public void itShouldThrowIfUsedCPFIsPassedOnCreation() {
        when(repository.findByCpf(this.cpf)).thenReturn(Optional.of(new Patient()));

        Exception e = assertThrows(InvalidFieldException.class, () -> {
            service.createPatient(createDTO());
        });

        assertTrue(e.getMessage().contains(Constants.IN_USE));
    }

    @Test public void itShouldCreateSuccessfully() {
        assertDoesNotThrow(() -> {
            service.createPatient(createDTO());
        });
    }

    @Test public void itShouldThrowIfIdIsInvalidOnUpdate() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception e = assertThrows(InvalidFieldException.class, () -> {
            service.updatePatient(id, createDTO());
        });
        assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
    }

    @Test public void itShouldThrowIfPassedDTOIsNullOnUpdate() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.updatePatient(id, null);
        });
    }

    @Test public void itShoulfThrowIfPatientIsNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        Exception e = assertThrows(InvalidFieldException.class, () -> {
           service.updatePatient(id, createDTO());
        });
        assertTrue(e.getMessage().contains(Constants.INVALID_REFERENCE));
    }

    @Test public void itShouldThrowIfCpfIsInvalidOnUpdate() {
        when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
        this.cpf = "12345678910123";

        Exception e = assertThrows(InvalidFieldException.class, () -> {
            service.updatePatient(id, createDTO());
        });
        assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT));
    }

    @Test public void itShouldThrowIfCpfIsInUseOnUpdate() {
        when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
        when(repository.findByCpf(this.cpf)).thenReturn(Optional.of(new Patient(UUID.randomUUID())));

        Exception e = assertThrows(InvalidFieldException.class, () -> {
            service.updatePatient(id, createDTO());
        });
        assertTrue(e.getMessage().contains(Constants.IN_USE));
    }

    @Test public void itShouldCallRepositorySaveOnUpdateSuccess() {
        when(repository.findById(any())).thenReturn(Optional.of(new Patient()));
        when(repository.findByCpf(this.cpf)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            service.updatePatient(id, createDTO());
            verify(repository).save(any());
        });
    }

    @Test public void itShouldThrowIfPatientIsNotFoundOnDelete() {
        assertThrows(InvalidFieldException.class, () -> {
           service.deletePatient(this.id);
        });
    }

    @Test public void itShouldNotUpdateIfPatientIsAlreadyInactiveOnDelete() {
        Patient p = new Patient();
        p.setIsActive(Boolean.FALSE);
        when(repository.findById(any())).thenReturn(Optional.of(p));

        service.deletePatient(this.id);

        verify(repository, times(0)).save(any());
    }

    @Test public void itShouldUpdateIfPatientIsAlreadyInactiveOnDelete() {
        Patient p = new Patient();
        p.setIsActive(Boolean.TRUE);
        when(repository.findById(any())).thenReturn(Optional.of(p));

        service.deletePatient(this.id);

        verify(repository).save(any());
    }
}