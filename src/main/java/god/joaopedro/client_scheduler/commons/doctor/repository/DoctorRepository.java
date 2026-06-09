package god.joaopedro.client_scheduler.commons.doctor.repository;

import god.joaopedro.client_scheduler.commons.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    public Optional<Doctor> findByCpf(String cpf);
    public Optional<Doctor> findByCrm(String crm);
}
