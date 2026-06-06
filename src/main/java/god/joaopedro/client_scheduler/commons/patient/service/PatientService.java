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

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository repository;

    @Transactional
    public Patient createPatient(PatientDTO patientDTO) {
        if(patientDTO == null){
            throw new IllegalArgumentException("Patient must not be null.");
        }

        if(!CpfValidator.validate(patientDTO.cpf()))
            throw new InvalidFieldException(Constants.CPF, Constants.INVALID_MESSAGE);

        if(repository.findByCpf(patientDTO.cpf()).isPresent())
            throw new InvalidFieldException(Constants.CPF, Constants.IN_USE);

        return repository.save(new Patient(patientDTO));
    }

    public Patient getByCpf(String cpf) {
        return repository.findByCpf(cpf).orElse(null);
    }

    public List<Patient> listPatients() {
        return repository.findAll();
    }

}
