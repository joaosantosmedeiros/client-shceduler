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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository repository;

    @InjectMocks
    private DoctorService service;

    protected String cpf = "70431492077";
    private String crm = "157/RJ";

    private DoctorDTO createDTO() {
        return new DoctorDTO(null, cpf, crm, null, null);
    }

    @Nested
    class OnCreate{

        @Test
        public void itShouldThrowIfNullDTOIsProvided() {
            assertThrows(IllegalArgumentException.class, () -> service.create(null));
        }

        @Test
        public void itShouldThrowIfInvalidCpfIsProvided(){
            cpf = "70431492078";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CPF));
        }

        @Test
        public void itShouldThrowIfInvalidCrmIsProvided(){
            crm = "123/xp";

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.INVALID_OBJECT) && e.getField().equals(Constants.CRM));
        }

        @Test public void itShouldThrowIfCpfIsInUse() {
            when(repository.findByCpf(any())).thenReturn(Optional.of(new Doctor()));

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().contains(Constants.CPF));
        }

        @Test public void itShouldThrowIfCrmIsInUse() {
            when(repository.findByCrm(any())).thenReturn(Optional.of(new Doctor()));
            when(repository.findByCpf(any())).thenReturn(Optional.empty());

            InvalidFieldException e = assertThrows(InvalidFieldException.class, () -> service.create(createDTO()));
            assertTrue(e.getMessage().contains(Constants.IN_USE) && e.getField().contains(Constants.CRM));
        }
    }
}