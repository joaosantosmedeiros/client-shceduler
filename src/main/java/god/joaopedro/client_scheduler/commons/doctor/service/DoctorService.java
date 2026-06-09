package god.joaopedro.client_scheduler.commons.doctor.service;

import god.joaopedro.client_scheduler.commons.doctor.model.Doctor;
import god.joaopedro.client_scheduler.commons.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository repository;

    public Doctor getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public List<Doctor> list() {
        return repository.findAll();
    }

}
