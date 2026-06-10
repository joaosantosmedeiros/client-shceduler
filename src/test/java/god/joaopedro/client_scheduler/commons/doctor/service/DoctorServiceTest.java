package god.joaopedro.client_scheduler.commons.doctor.service;

import god.joaopedro.client_scheduler.commons.doctor.model.Doctor;
import god.joaopedro.client_scheduler.commons.doctor.model.dto.DoctorDTO;
import god.joaopedro.client_scheduler.commons.doctor.repository.DoctorRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository repository;

    @InjectMocks
    private DoctorService service;

    private UUID id = UUID.randomUUID();
    private String cpf = "70431492077";
    private String crm = "157/RJ";

    private DoctorDTO createDTO() {
        return new DoctorDTO(null, cpf, crm, null, null);
    }

    @Nested
    class OnCreate{

        @Test void itShouldThrowIfNullDTOIsProvided() {
            assertThrows(IllegalArgumentException.class, () -> service.create(null));
        }

        @Test void itShouldThrowIfInvalidCpfIsProvided(){
            cpf = "70431492078";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CPF));
        }

        @Test void itShouldThrowIfInvalidCrmIsProvided(){
            crm = "123/xp";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CRM));
        }

        @Test void itShouldThrowIfCpfIsInUse() {
            when(repository.findByCpf(any())).thenReturn(Optional.of(new Doctor()));

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().contains(Constants.CPF));
        }

        @Test void itShouldThrowIfCrmIsInUse() {
            when(repository.findByCrm(any())).thenReturn(Optional.of(new Doctor()));
            when(repository.findByCpf(any())).thenReturn(Optional.empty());

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().contains(Constants.CRM));
        }
    }

    @Nested
    class OnUpdate {

        @Test void itShouldThrowIfDtoIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.update(id, null));
        }

        @Test void itShouldThrowIfDoctorIsNotFound() {
            when(repository.findById(any())).thenReturn(Optional.empty());

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertEquals(Constants.ID, e.getField());
        }

        @Test void itShouldThrowIfCpfIsInvalid() {
            when(repository.findById(any())).thenReturn(Optional.of(new Doctor(createDTO())));
            cpf = "54155897081";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CPF));
        }

        @Test void itShouldThrowIfCpfIsInUse() {
            when(repository.findById(any())).thenReturn(Optional.of(new Doctor(createDTO())));
            when(repository.findByCpf(any())).thenReturn(Optional.of(new Doctor()));
            cpf = "54155897083";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().equals(Constants.CPF));
        }

        @Test void itShouldThrowIfCrmIsInvalid() {
            when(repository.findById(any())).thenReturn(Optional.of(new Doctor(createDTO())));
            crm = "ab/py";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CRM));
        }

        @Test void itShouldThrowIfCrmIsInUse() {
            when(repository.findById(any())).thenReturn(Optional.of(new Doctor(createDTO())));
            when(repository.findByCrm(any())).thenReturn(Optional.of(new Doctor()));
            crm = "778/RR";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.update(id, createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().equals(Constants.CRM));
        }
    }
}