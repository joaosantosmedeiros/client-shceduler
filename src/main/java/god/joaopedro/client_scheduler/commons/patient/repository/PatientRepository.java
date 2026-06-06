package god.joaopedro.client_scheduler.commons.patient.repository;

import god.joaopedro.client_scheduler.commons.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByCpf(String cpf);
}
