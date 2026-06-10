package god.joaopedro.client_scheduler.commons.patient.service;

import god.joaopedro.client_scheduler.commons.patient.model.Patient;
import god.joaopedro.client_scheduler.commons.patient.model.dto.PatientDTO;
import god.joaopedro.client_scheduler.commons.patient.repository.PatientRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import god.joaopedro.client_scheduler.utils.CpfValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository repository;

    @Transactional
    public Patient create(PatientDTO dto) {
        if(dto == null){
            throw new IllegalArgumentException("paciente não deve ser nulo");
        }

        if(!CpfValidator.validate(dto.cpf()))
            throw new InvalidFieldException(Constants.CPF, Constants.INVALID_OBJECT);

        if(repository.findByCpf(dto.cpf()).isPresent())
            throw new InvalidFieldException(Constants.CPF, Constants.IN_USE);

        return repository.save(new Patient(dto));
    }

    public Patient getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public List<Patient> list() {
        return repository.findAll();
    }

    public Patient update(UUID id, PatientDTO dto) {

        if(id == null || dto == null){
            throw new IllegalArgumentException("paciente não deve ser nulo");
        }

        Patient patient = repository.findById(id).
                orElseThrow(() -> new InvalidFieldException(Constants.ID, Constants.INVALID_REFERENCE));

        if(!CpfValidator.validate(dto.cpf()))
            throw new InvalidFieldException(Constants.CPF, Constants.INVALID_OBJECT);

        var cpfIsInUse = repository.findByCpf(dto.cpf());
        if(cpfIsInUse.isPresent() && !cpfIsInUse.get().getId().equals(id))
            throw new InvalidFieldException(Constants.CPF, Constants.IN_USE);

        patient.setName(dto.name());
        patient.setCpf(dto.cpf());
        patient.setPhone(dto.phone());
        patient.setBirthDate(dto.birthDate());
        patient.setUpdatedAt(LocalDateTime.now());

        return repository.save(patient);
    }

    public void delete(UUID id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new InvalidFieldException(Constants.ID, Constants.INVALID_REFERENCE));

        if(patient.getIsActive()){
            patient.setIsActive(Boolean.FALSE);
            patient.setUpdatedAt(LocalDateTime.now());
            repository.save(patient);
        }
    }

    public void activate(UUID id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new InvalidFieldException(Constants.ID, Constants.INVALID_REFERENCE));

        if(!patient.getIsActive()){
            patient.setIsActive(Boolean.TRUE);
            patient.setUpdatedAt(LocalDateTime.now());
            repository.save(patient);
        }
    }
}
