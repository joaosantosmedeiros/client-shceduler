package god.joaopedro.client_scheduler.commons.speciality.repository;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpecialityRepository extends JpaRepository<Speciality, UUID> {

    Optional<Speciality> findByName(String name);
}
